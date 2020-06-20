package com.example

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.util.error

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        gson {
        }
    }
    install(StatusPages) {
        statusFile(
                HttpStatusCode.InternalServerError,
                HttpStatusCode.NotFound,
                filePattern = "customerrors/myerror#.html"
        )
        exception<MyFirstException> { cause ->
            call.respond(HttpStatusCode.Unauthorized)
            throw cause
        }
        exception<MySecondException> { cause ->
            call.respondRedirect("/", false)
        }
        exception<MyThirdException> { cause ->
            call.respondText("The third exception just happened... Please fix it! :-)")
            throw cause
        }
    }

    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }



        get("/first"){
            throw MyFirstException()
        }
        get("/second"){
            throw MySecondException()
        }
        get("/third"){
            throw MyThirdException()
        }

        get("/json/gson") {
            call.respond(mapOf("hello" to "world"))
        }
    }
}

class MyFirstException : RuntimeException()
class MySecondException : RuntimeException()
class MyThirdException : RuntimeException()


