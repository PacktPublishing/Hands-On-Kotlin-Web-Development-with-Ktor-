package com.example

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        var weather = "sunny"
        get("/weatherforecast"){
            call.respondText(weather, contentType = ContentType.Text.Plain)
        }
        post("/weatherforecast"){
            weather = call.receiveText()
            call.respondText("The weather has been set to: $weather",
                contentType = ContentType.Text.Plain)
        }
    }
}

