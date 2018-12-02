package io.chiu.app

import io.chiu.app.configuration.onEnvironment
import io.chiu.app.features.enableCORS
import io.chiu.app.features.enableExceptionHandling
import io.chiu.app.features.enableHTTPSOnly
import io.chiu.app.features.enableJsonSerialization
import io.chiu.app.features.enableLogging
import io.chiu.app.features.enableWebSockets
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.netty.EngineMain
import io.ktor.util.KtorExperimentalAPI
import io.ktor.websocket.webSocket
import java.time.LocalDateTime
import java.util.UUID

fun main(args: Array<String>): Unit = EngineMain.main(args)

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
        get("/listen") {
            call.respond(
                Noise(
                    uuid = UUID.randomUUID(),
                    level = (0..120).random(),
                    timestamp = LocalDateTime.now()
                )
            )
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
