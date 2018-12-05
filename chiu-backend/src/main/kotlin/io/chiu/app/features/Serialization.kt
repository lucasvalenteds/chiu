package io.chiu.app.features

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson

fun Application.enableJsonSerialization() {
    install(ContentNegotiation) {
        jackson { setup() }
    }
}

fun getJsonMapper(): ObjectMapper = jacksonObjectMapper().setup()

private fun ObjectMapper.setup(): ObjectMapper {
    registerModule(JavaTimeModule())
    disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    return this
}