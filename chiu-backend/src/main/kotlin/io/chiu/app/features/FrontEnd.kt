package io.chiu.app.features

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.response.respondBytes
import io.ktor.routing.Routing
import io.ktor.routing.get

fun Routing.serveFrontEnd(filesFolder: String, indexFile: ByteArray) {
    get("/") {
        call.respondBytes(indexFile, ContentType.Text.Html, HttpStatusCode.OK)
    }
    static {
        resources(filesFolder)
    }
}