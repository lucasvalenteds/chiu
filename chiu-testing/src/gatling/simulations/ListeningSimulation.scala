import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.action.sse.SseConnectBuilder
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration._

class ListeningSimulation extends Simulation {

  val httpConf: HttpProtocolBuilder = Configurations.httpConfiguration
    .header("Accept", "text/event-stream")

  val callServer: SseConnectBuilder = sse("listen-open")
    .connect("/listen")
    .await(30.seconds) {
      sse
        .checkMessage("noise-id")
        .check(jsonPath("$.event").is("noise"))
        .check(jsonPath("$.data").exists)
    }

  val listeningScenario: ScenarioBuilder = scenario(this.getClass.getSimpleName)
    .pause(30.seconds)
    .exec(callServer)

  setUp(
    listeningScenario
      .inject(
        incrementConcurrentUsers(5)
          .times(5)
          .eachLevelLasting(10.seconds)
          .separatedByRampsLasting(10.seconds)
          .startingFrom(10)
      ))
    .assertions(
      details("listen-open").successfulRequests.percent.gte(98)
    )
    .protocols(httpConf)
}
