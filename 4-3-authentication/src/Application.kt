package com.example

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.auth.*
import io.ktor.gson.*
import io.ktor.features.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(Authentication) {
        basic("myBasicAuth1") {
            realm = "My Realm"
            validate { if (it.name == "mike" && it.password == "password") UserIdPrincipal(it.name) else null }
        }
        basic("myBasicAuth2") {
            realm = "My Other Realm"
            validate { if (it.password == "${it.name}abc123") UserIdPrincipal(it.name) else null }
        }
    }

    install(ContentNegotiation) {
        gson {
        }
    }

    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        authenticate("myBasicAuth1") {
            get("/secret/weather") {
                val principal = call.principal<UserIdPrincipal>()!!
                call.respondText("Dear ${principal.name}, it looks like sun tomorrow")
            }
        }
        authenticate("myBasicAuth2") {
            get("/trendycolor/nextmonth") {
                val principal = call.principal<UserIdPrincipal>()!!
                call.respondText("Hi ${principal.name} we think that purple will be popular next month")
            }
        }

        get("/json/gson") {
            call.respond(mapOf("hello" to "world"))
        }
    }
}

