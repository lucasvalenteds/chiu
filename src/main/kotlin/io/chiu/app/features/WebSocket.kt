package io.chiu.app.features

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.websocket.WebSockets
import java.time.Duration

fun Application.enableWebSockets() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
}