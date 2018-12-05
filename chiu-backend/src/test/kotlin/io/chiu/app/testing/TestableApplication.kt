package io.chiu.app.testing

import io.chiu.app.NoiseEvent
import io.chiu.app.configuration.Database
import io.chiu.app.module
import io.ktor.application.Application
import io.ktor.config.MapApplicationConfig
import io.ktor.server.testing.TestApplicationRequest
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel

@ExperimentalCoroutinesApi
@KtorExperimentalAPI
fun Application.testableModule(database: Database, noiseChannel: Channel<NoiseEvent>) {
    (environment.config as MapApplicationConfig).apply {
        put("ktor.environment", "dev")
        put("database.name", "chiutest")
        put("database.url", "mongodb://localhost:27018/chiutest")
    }
    module(database = database, noiseChannel = noiseChannel)
}

internal fun TestApplicationRequest.triggerEvents() = Unit
