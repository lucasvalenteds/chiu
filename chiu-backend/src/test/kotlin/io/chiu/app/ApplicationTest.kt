package io.chiu.app

import io.chiu.app.configuration.Database
import io.chiu.app.features.JSON
import io.chiu.app.testing.testableModule
import io.kotlintest.fail
import io.kotlintest.specs.StringSpec
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import io.ktor.server.testing.withTestApplication
import io.ktor.util.KtorExperimentalAPI
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@KtorExperimentalAPI
class ApplicationTest : StringSpec({

    val database = mockk<Database>(relaxed = true)
    val noiseChannel = BroadcastChannel<NoiseEvent>(Channel.CONFLATED)
    val noise = NoiseLevel(level = (0..150).random())

    "SSE /listen streams the current noise level" {
        fail("No idea how to test SSE for the current implementation")
    }
    "WS / persists the current noise level" {
        withTestApplication({ testableModule(database, noiseChannel) }) {
            handleWebSocketConversation("/") { incoming, outgoing ->
                val level = JSON.writeValueAsString(noise)
                outgoing.send(Frame.Text(level))

                val responseMessage = (incoming.receive() as Frame.Text).readText()

                expectThat(responseMessage).isEqualTo("""{"status":"OK"}""")
                coVerify(exactly = 1) { database.saveNoiseReport(any()) }
            }
        }
    }
})
