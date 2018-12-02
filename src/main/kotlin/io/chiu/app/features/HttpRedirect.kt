package io.chiu.app.features

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.HttpsRedirect

fun Application.enableHTTPSOnly() {
    install(HttpsRedirect) {
        // The port to redirect to. By default 443, the default HTTPS port.
        sslPort = 443
        // 301 Moved Permanently, or 302 Found redirect.
        permanentRedirect = true
    }
}