package io.chiu.app

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.module.kotlin.readValue
import com.mongodb.MongoException
import com.mongodb.reactivestreams.client.MongoClients
import io.chiu.app.configuration.Database
import io.chiu.app.configuration.DatabaseNoSQL
import io.chiu.app.configuration.onEnvironment
import io.chiu.app.features.JSON
import io.chiu.app.features.enableCORS
import io.chiu.app.features.enableExceptionHandling
import io.chiu.app.features.enableHTTPSOnly
import io.chiu.app.features.enableJsonSerialization
import io.chiu.app.features.enableLogging
import io.chiu.app.features.enableWebSockets
import io.chiu.app.features.serveFrontEnd
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.log
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.cio.websocket.CloseReason
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.close
import io.ktor.http.cio.websocket.readText
import io.ktor.response.header
import io.ktor.response.respondTextWriter
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.netty.EngineMain
import io.ktor.util.KtorExperimentalAPI
import io.ktor.websocket.webSocket
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.channels.ClosedSendChannelException
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.filter
import org.bson.types.ObjectId
import java.time.Instant
import java.util.Date

fun main(args: Array<String>): Unit = EngineMain.main(args)

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@KtorExperimentalAPI
@Suppress("unused")
fun Application.module(
    database: Database = DatabaseNoSQL(
        MongoClients.create(environment.config.property("database.url").getString())
            .getDatabase(environment.config.property("database.name").getString())
            .getCollection("noises")
    ),
    noiseChannel: BroadcastChannel<NoiseEvent> = BroadcastChannel(Channel.CONFLATED),
    frontEndFolder: String = environment.config.property("frontend.folder").getString(),
    frontEndIndex: ByteArray = ClassLoader.getSystemResourceAsStream("$frontEndFolder/index.html").readBytes()
) {
    enableJsonSerialization()
    enableLogging()
    enableCORS()
    onEnvironment(production = {
        enableHTTPSOnly()
    })
    enableWebSockets()

    routing {
        webSocket("/") {
            incoming.filter { it is Frame.Text }.consumeEach {
                try {
                    val report = NoiseReport(
                        id = ObjectId().toString(),
                        level = JSON.readValue<NoiseLevel>((it as Frame.Text).readText()).level,
                        timestamp = Date.from(Instant.now())
                    )

                    database.saveNoiseReport(report)
                    noiseChannel.send(NoiseEvent("noise", report))
                    outgoing.send(Frame.Text("""{"status":"OK"}"""))
                } catch (exception: ClosedReceiveChannelException) {
                    log.info("101: " + exception.message)
                    close(CloseReason(CloseReason.Codes.NORMAL, "Client closed the connection"))
                } catch (exception: JsonParseException) {
                    log.error("102: " + exception.message)
                    close(CloseReason(CloseReason.Codes.CANNOT_ACCEPT, "Payload sent has syntax error"))
                } catch (exception: JsonMappingException) {
                    log.error("103: " + exception.message)
                    close(CloseReason(CloseReason.Codes.CANNOT_ACCEPT, "Payload doesn't have valid content"))
                } catch (exception: MongoException) {
                    log.error("104: " + exception.message)
                    close(CloseReason(CloseReason.Codes.TRY_AGAIN_LATER, "Database is not available right now"))
                } catch (exception: ClosedSendChannelException) {
                    log.error("105: " + exception.message)
                    close(CloseReason(CloseReason.Codes.UNEXPECTED_CONDITION, "Internal server error"))
                }
            }
        }
        get("/listen") {
            call.response.header(HttpHeaders.CacheControl, "no-cache")
            call.respondTextWriter(contentType = ContentType.parse("text/event-stream")) {
                noiseChannel.consumeEach { sseEvent ->
                    write("event: ${sseEvent.event}\n")
                    write("data: ${JSON.writeValueAsString(sseEvent.data)}\n")
                    write("\n")
                    flush()
                    log.trace(sseEvent.toString())
                }
            }
        }
        serveFrontEnd(frontEndFolder, frontEndIndex)
        enableExceptionHandling()
    }
}

data class NoiseLevel(val level: Int)
data class NoiseReport(val id: String, val level: Int, val timestamp: Date)
data class NoiseEvent(val event: String, val data: NoiseReport)
