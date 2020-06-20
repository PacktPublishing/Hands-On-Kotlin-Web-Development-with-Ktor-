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
    install(DefaultHeaders){
        header("MySystemName", "BookstoreApp")
    }

    install(ContentNegotiation) {
        gson {
        }
    }

    routing {
        get("/") {
            for (h in call.request.headers.entries()){
                log.info("header: ${h.key} : ${h.value}")
            }
            call.response.header("Myheader...", "Myheadervalue")
            call.response.header(HttpHeaders.SetCookie, "cookieToUse")
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        get("/json/gson") {
            call.respond(mapOf("hello" to "world"))
        }
    }
}

