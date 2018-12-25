package io.chiu.app.features

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod

fun Application.enableCORS() {
    install(CORS) {
        anyHost()
    }
}