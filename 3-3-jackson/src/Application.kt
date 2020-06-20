package com.example

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import com.fasterxml.jackson.databind.*
import io.ktor.jackson.*
import io.ktor.features.*
import java.time.*
import com.fasterxml.jackson.datatype.jsr310.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        jackson {
            registerModule(JavaTimeModule())
            enable(SerializationFeature.INDENT_OUTPUT)
            enable(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED)
        }
    }

    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        get("/json/jackson") {
            call.respond(mapOf("hello" to "world"))
        }

        get("/ship"){
            call.respond(SpaceShip(null, 80, listOf("Mike")))
        }
    }
}

data class SpaceShip(
    @JsonInclude(value=JsonInclude.Include.NON_NULL)
    val name: String?,
    val fuel: Int,
    val crew: List<String>,
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    val launchDate: LocalDateTime = LocalDateTime.now())


