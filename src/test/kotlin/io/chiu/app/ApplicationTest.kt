package io.chiu.app

import io.chiu.app.configuration.Database
import io.chiu.app.features.getJsonMapper
import io.chiu.app.testing.testableModule
import io.kotlintest.specs.FeatureSpec
import io.ktor.http.cio.websocket.Frame
import io.ktor.server.testing.withTestApplication
import io.ktor.util.KtorExperimentalAPI
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.Channel
import strikt.api.expectThat
import strikt.assertions.isNotNull

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@KtorExperimentalAPI
class ApplicationTest : FeatureSpec({
    feature("Emitting current noise level via SSE") {
        scenario("GET /listen works") {
            // No idea how to test SSE for the current implementation
        }
    }

    feature("Reporting current noise level via WS") {
        scenario("/report with valid payload works") {
            val database = mockk<Database>()
            val noiseChannel = Channel<NoiseEvent>()

            withTestApplication({ testableModule(database, noiseChannel) }) {
                handleWebSocketConversation("/listen") { _, outgoing ->
                    val levelToReport = NoiseLevel(level = (0..120).random())
                        .let(getJsonMapper()::writeValueAsString)
                        .let(Frame::Text)

                    outgoing.send(levelToReport)

                    coVerify(exactly = 1) { database.saveNoiseReport(any()) }
                    expectThat(noiseChannel.receiveOrNull()).isNotNull()
                }
            }
        }
    }
})
