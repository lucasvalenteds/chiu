package io.chiu.app

import io.ktor.application.Application
import io.ktor.config.MapApplicationConfig
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.util.KtorExperimentalAPI

@KtorExperimentalLocationsAPI
@KtorExperimentalAPI
fun Application.testableModule() {
    (environment.config as MapApplicationConfig).apply {
        put("ktor.environment", "dev")
    }
    module()
}