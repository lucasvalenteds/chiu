package io.chiu.app

import io.chiu.app.configuration.Database
import io.chiu.app.features.getJsonMapper
import io.chiu.app.testing.testableModule
import io.kotlintest.fail
import io.kotlintest.specs.StringSpec
import io.ktor.http.cio.websocket.Frame
import io.ktor.server.testing.withTestApplication
import io.ktor.util.KtorExperimentalAPI
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import strikt.api.expectThat
import strikt.assertions.isNotNull

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@KtorExperimentalAPI
@Suppress("UNREACHABLE_CODE", "UNUSED_VARIABLE")
class ApplicationTest : StringSpec({

    val database = mockk<Database>(relaxed = true)
    val noiseChannel = Channel<NoiseEvent>()
    val noise = NoiseLevel(level = (0..150).random())

    "SSE /listen streams the current noise level" {
        fail("No idea how to test SSE for the current implementation")
    }
    "WS / persists the current noise level" {
        fail("Test hanging for unknown reason")

        withTestApplication({ testableModule(database, noiseChannel) }) {
            launch {
                handleWebSocketConversation("/") { _, outgoing ->
                    val level = getJsonMapper().writeValueAsString(noise)
                    outgoing.send(Frame.Text(level))
                }
            }
            runBlocking {
                expectThat(noiseChannel.receiveOrNull()).isNotNull()
                coVerify(exactly = 1) { database.saveNoiseReport(any()) }
            }
        }
    }
})
