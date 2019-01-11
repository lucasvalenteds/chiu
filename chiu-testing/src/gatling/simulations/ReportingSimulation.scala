import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.action.ws.WsSendTextFrameBuilder
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration._

class ReportingSimulation extends Simulation {

  val wsConf: HttpProtocolBuilder = Configurations.wsConfiguration

  val sendNoiseLevelToServer: WsSendTextFrameBuilder = ws("report")
    .sendText("""{"level":120}""")
    .await(30.seconds) {
      ws.checkTextMessage("status").check(jsonPath("$.status").is("OK"))
    }

  val reportingScenario: ScenarioBuilder = scenario(this.getClass.getSimpleName)
    .exec(ws("report").connect("/"))
    .repeat(30) {
      exec(sendNoiseLevelToServer)
    }
    .exec(ws("report").close)

  setUp(
    reportingScenario
      .inject(
        incrementConcurrentUsers(5)
          .times(5)
          .eachLevelLasting(10.seconds)
          .separatedByRampsLasting(10.seconds)
          .startingFrom(10)
      )).protocols(wsConf)
}
