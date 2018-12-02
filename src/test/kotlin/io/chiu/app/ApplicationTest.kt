package io.chiu.app

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@ExperimentalCoroutinesApi
@KtorExperimentalAPI
class ApplicationTest {
    @Test
    fun testListen(): Unit = withTestApplication({ testableModule() }) {
        handleRequest(HttpMethod.Get, "/listen").apply {
            assertEquals(HttpStatusCode.OK, response.status())
            assertNotNull(response.content)
        }
    }
}
