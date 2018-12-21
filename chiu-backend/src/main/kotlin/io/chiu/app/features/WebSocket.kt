package io.chiu.app.features

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.websocket.WebSockets

fun Application.enableWebSockets() {
    install(WebSockets)
}