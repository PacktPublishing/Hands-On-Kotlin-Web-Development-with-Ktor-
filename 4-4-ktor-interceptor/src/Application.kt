package com.example

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.gson.*
import io.ktor.features.*
import io.ktor.util.pipeline.PipelinePhase

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        gson {
        }
    }
    val mike = PipelinePhase("Mike")
    insertPhaseBefore(ApplicationCallPipeline.Call, mike)
    intercept(ApplicationCallPipeline.Setup) {
        log.info("Setup phase")
    }
    intercept(ApplicationCallPipeline.Call) {
        log.info("Call phase")
    }
    intercept(ApplicationCallPipeline.Features) {
        log.info("Features phase")
    }
    intercept(ApplicationCallPipeline.Monitoring) {
        log.info("Monitoring phase")
    }
    intercept(ApplicationCallPipeline.Fallback) {
        log.info("Fallback phase")
    }
    intercept(mike){
        log.info("Mike phase... ${call.request.uri}")
        if (call.request.uri.contains("mike")){
            log.info("The uri contains mike")
            call.respondText("The endpoint contains Mike")
            finish()
        }
    }
    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }
        get("/something/mike/anotherthing") {
            call.respondText("This endpoint is handled by the route. Have a nice day!", contentType = ContentType.Text.Plain)
        }

    }
}

