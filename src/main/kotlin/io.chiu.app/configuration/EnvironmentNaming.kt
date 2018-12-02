package io.chiu.app.configuration

import io.ktor.application.Application
import io.ktor.util.KtorExperimentalAPI

@KtorExperimentalAPI
fun Application.onEnvironment(development: () -> Unit = {}, production: () -> Unit = {}) =
    when (environment.config.property("ktor.environment").getString()) {
        "dev" -> development()
        "prod" -> production()
        else -> Unit
    }
