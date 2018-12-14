package io.chiu.app.testing

import io.chiu.app.NoiseEvent
import io.chiu.app.configuration.Database
import io.chiu.app.module
import io.ktor.application.Application
import io.ktor.config.MapApplicationConfig
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel

@ExperimentalCoroutinesApi
@KtorExperimentalAPI
fun Application.testableModule(
    database: Database = InMemoryDatabase(),
    noiseChannel: Channel<NoiseEvent> = Channel(),
    frontEndFolder: String = "frontend",
    frontEndIndex: ByteArray = ClassLoader.getSystemResourceAsStream("$frontEndFolder/index.html").readBytes()
) {
    (environment.config as MapApplicationConfig).apply {
        put("ktor.environment", "dev")
        put("database.name", "chiutest")
        put("database.url", "mongodb://localhost:27018/chiutest")
        put("frontend.folder", "frontend")
    }
    module(
        database = database,
        frontEndFolder = frontEndFolder,
        frontEndIndex = frontEndIndex,
        noiseChannel = noiseChannel
    )
}
