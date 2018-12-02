package io.chiu.app

import io.ktor.application.Application
import io.ktor.config.MapApplicationConfig
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@KtorExperimentalAPI
fun Application.testableModule() {
    (environment.config as MapApplicationConfig).apply {
        put("ktor.environment", "dev")
    }
    module()
}