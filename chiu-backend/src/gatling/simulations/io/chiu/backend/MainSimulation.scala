package io.chiu.backend

import com.typesafe.config.{Config, ConfigFactory}
import io.gatling.core.Predef._
import io.gatling.core.feeder.{Feeder, Record}
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.Random

class MainSimulation extends Simulation { 
  val environment: Config = ConfigFactory.load()

  val protocol: HttpProtocolBuilder = http
    .wsBaseUrl(environment.getString("chiu.ingest.url"))
    .baseUrl(environment.getString("chiu.export.url"))
    .acceptHeader("text/event-stream")

  val levelFeeder: Iterator[Record[String]] = new Feeder[String] {

    private val random: Random = new Random()

    override def hasNext: Boolean = true

    override def next(): Map[String, String] = {
      Map(
        "level" -> random.nextInt(150).toString
      )
    }
  }

  val ingest: ScenarioBuilder = scenario("WebSocket")
    .feed(levelFeeder)
    .exec(ws("Connect").connect("/"))
    .exec(ws("Send sensor data").sendText("{\"level\":${level}}"))
    .pause(environment.getInt("test.delayBetweenRequestsMilliseconds") millis)

  val export: ScenarioBuilder = scenario("SSE")
    .exec(
      sse("Export").connect("/export")
        .await(environment.getInt("test.delayBetweenResponseCheckSeconds") seconds)(
          sse.checkMessage("Event name is `noise`")
            .check(regex(""""event":"noise(.*)""""))
        )
    )

  setUp(
    ingest.inject(constantConcurrentUsers(
      environment.getInt("test.amountOfDashboardsAtSameTime")) during (environment.getInt("test.executionTimeSeconds") seconds)
    ),
    export.inject(
      atOnceUsers(environment.getInt("test.amountOfSensorsAtSameTime"))
    )
  )
    .assertions(
      global.responseTime.mean.lte(environment.getInt("test.responseTimeMilliseconds")),
      global.successfulRequests.percent.gt(environment.getInt("test.responseSuccessRatePercentage"))
    )
    .protocols(protocol)
}
