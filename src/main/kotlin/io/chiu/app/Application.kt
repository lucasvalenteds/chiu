package io.chiu.app

import io.chiu.app.configuration.onEnvironment
import io.chiu.app.features.enableCORS
import io.chiu.app.features.enableExceptionHandling
import io.chiu.app.features.enableHTTPSOnly
import io.chiu.app.features.enableJsonSerialization
import io.chiu.app.features.enableLogging
import io.chiu.app.features.enableWebSockets
import io.chiu.app.features.getJsonMapper
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.call
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
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.util.UUID

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

    val channel: ReceiveChannel<Noise> = produce {
        while (true) {
            send(Noise(UUID.randomUUID(), (0..120).random(), LocalDateTime.now()))
            delay(500)
        }
    }

    routing {
        get("/listen") {
            call.respondSse(channel)
        }
        enableExceptionHandling()
        webSocket("/echo") {
            send(Frame.Text("Hi from server"))
            while (true) {
                val frame = incoming.receive()
                if (frame is Frame.Text) {
                    send(Frame.Text("Client said: " + frame.readText()))
                }
            }
        }
    }
}

data class Noise(
    val uuid: UUID,
    val level: Int,
    val timestamp: LocalDateTime
)

private suspend fun ApplicationCall.respondSse(events: ReceiveChannel<Noise>) {
    response.header(HttpHeaders.CacheControl, "no-cache")
    respondTextWriter(contentType = ContentType.parse("text/event-stream")) {
        for (event in events) {
            write(getJsonMapper().writeValueAsString(event).plus("\n"))
            flush()
        }
    }
}