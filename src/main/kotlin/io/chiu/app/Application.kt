package io.chiu.app

import com.fasterxml.jackson.module.kotlin.readValue
import io.chiu.app.configuration.onEnvironment
import io.chiu.app.features.enableCORS
import io.chiu.app.features.enableExceptionHandling
import io.chiu.app.features.enableHTTPSOnly
import io.chiu.app.features.enableJsonSerialization
import io.chiu.app.features.enableLogging
import io.chiu.app.features.enableWebSockets
import io.chiu.app.features.getJsonMapper
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.log
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import io.ktor.response.header
import io.ktor.response.respondTextWriter
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.netty.EngineMain
import io.ktor.util.KtorExperimentalAPI
import io.ktor.websocket.webSocket
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import org.bson.types.ObjectId
import java.time.Instant
import java.util.Date

fun main(args: Array<String>): Unit = EngineMain.main(args)

@ExperimentalCoroutinesApi
@KtorExperimentalAPI
@Suppress("unused")
fun Application.module() {
    enableJsonSerialization()
    enableLogging()
    enableCORS()
    onEnvironment(production = {
        enableHTTPSOnly()
    })
    enableWebSockets()

    routing {
        val noiseChannel = Channel<NoiseEvent>()

        webSocket("/report") {
            var iteration = 0
            while (true) {
                val payload = incoming.receive() as Frame.Text
                val level = getJsonMapper().readValue<NoiseLevel>(payload.readText())
                val report = NoiseReport(ObjectId().toString(), level.level, Date.from(Instant.now()))
                val sseEvent = NoiseEvent(iteration, "noise", report)
                log.info("/report :: {}", sseEvent.toString())
                noiseChannel.send(sseEvent)
                iteration++
            }
        }

        get("/listen") {
            call.response.header(HttpHeaders.CacheControl, "no-cache")
            call.respondTextWriter(contentType = ContentType.parse("text/event-stream")) {
                for (sseEvent in noiseChannel) {
                    log.info("/listen :: {}", sseEvent.toString())
                    write("id: ${sseEvent.id}\n")
                    write("event: ${sseEvent.event}\n")
                    write("data: ${getJsonMapper().writeValueAsString(sseEvent.data)}\n")
                    write("\n")
                    flush()
                }
            }
        }
        enableExceptionHandling()
    }
}

data class NoiseLevel(val level: Int)
data class NoiseReport(val id: String, val level: Int, val timestamp: Date)
data class NoiseEvent(val id: Int, val event: String, val data: NoiseReport)
