package io.chiu.backend

import io.gatling.core.Predef._
import io.gatling.core.feeder.{Feeder, Record}
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.Random

class MainSimulation extends Simulation {

  val protocol: HttpProtocolBuilder = http
    .wsBaseUrl("ws://localhost:8080")
    .baseUrl("http://localhost:8080")
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

  val scene: ScenarioBuilder = scenario("WebSocket")
    .feed(levelFeeder)
    .exec(ws("Connect").connect("/"))
    .pause(1 second)
    .exec(ws("Send sensor data").sendText("${level}"))
    .pause(1 millis)

  setUp(scene.inject(constantUsersPerSec(5) during (1 minutes)))
    .assertions(
      global.responseTime.mean.lte(125),
      global.successfulRequests.percent.gt(95)
    )
    .protocols(protocol)
}