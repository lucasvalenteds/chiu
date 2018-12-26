package io.chiu.app.features

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CORS

fun Application.enableCORS() {
    install(CORS) {
        anyHost()
    }
}