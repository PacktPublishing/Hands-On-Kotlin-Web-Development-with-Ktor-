package com.example

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.gson.*
import io.ktor.features.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        gson {
        }
    }

    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }
        get("/library/book/{bookid}/checkout") {
            val bookid = call.parameters.get("bookid")
            call.respondText("You checked out the book $bookid", contentType = ContentType.Text.Plain)
        }
        get("/library/book/{bookid}/reserve") {
            val bookid = call.parameters.get("bookid")
            call.respondText("You reserved the book $bookid", contentType = ContentType.Text.Plain)
        }

        get("/library/book/{bookid}"){
            val bookid = call.parameters.get("bookid")
            val book = Book(bookid!!, "How to grow apples", "Mr. Appleton")
            val hypermedialinks = listOf<HypermediaLink>(
                HypermediaLink("http://localhost:8080/library/book/$bookid/checkout", "checkout", "GET"),
                HypermediaLink("http://localhost:8080/library/book/$bookid/reserve", "reserve", "GET")
            )
            val bookResponse =  BookResponse(book, hypermedialinks)
            call.respond(bookResponse)
        }

        get("/json/gson") {
            call.respond(mapOf("hello" to "world"))
        }
    }
}
data class Book(val id: String, val title: String, val author: String)
data class BookResponse(val book: Book, val links: List<HypermediaLink>)
data class HypermediaLink(val href: String, val rel: String, val type: String)


