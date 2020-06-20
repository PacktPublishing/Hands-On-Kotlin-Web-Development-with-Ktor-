package com.example

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.locations.Location
import io.ktor.locations.Locations
import io.ktor.locations.get
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(Locations) {
    }

    install(ContentNegotiation) {
        gson {
        }
    }

    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        get<MyLocation> {
            call.respondText("Location: name=${it.name}")
        }
        // Register nested routes
        get<Book.Author> {
            call.respondText("${it}")
        }
        get<Book.List> {
            call.respondText("$it")
        }
    }
}

@Location("/location/{name}")
class MyLocation(val name: String)

@Location("/book/{category}")
data class Book(val category: String) {
    @Location("/{author}")
    data class Author(val book: Book, val author: String)

    @Location("/list")
    data class List(val book: Book, val sortby: String, val asc: Int)
}

