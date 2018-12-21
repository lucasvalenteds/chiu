import com.typesafe.config.{Config, ConfigFactory}
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

object Configurations {
  val configurationFile: Config = ConfigFactory.load()
  val httpConfiguration: HttpProtocolBuilder =
    http.baseUrl(configurationFile.getString("chiu.api.url"))
}
