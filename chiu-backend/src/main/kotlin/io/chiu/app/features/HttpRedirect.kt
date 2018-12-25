package io.chiu.app.features

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.HttpsRedirect

fun Application.enableHTTPSOnly() {
    install(HttpsRedirect) {
        sslPort = 443
        permanentRedirect = true
    }
}