package com.example

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.application.log
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.jackson.jackson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

val mongoDataHandler = MongoDataHandler()

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        get("/allships") {
            call.respond(mongoDataHandler.allSpaceShips())
        }

        post("/fuelup/{spaceshipid}") {
            val shipid = call.parameters.get("spaceshipid")!!
            log.info("shipid $shipid")
            mongoDataHandler.fuelUpSpaceShip(shipid)
            call.respond(mongoDataHandler.allSpaceShips())
        }
        post("/replace") {
            log.info("ship replace started")
            val ship = call.receive(SpaceShip::class)
            log.info("ship $ship")
            mongoDataHandler.replaceSpaceShip(ship)
            call.respond(mongoDataHandler.findOneSpaceShip(ship.getIdAsHex())!!)
        }

        get("/sortedships") {
            val pageno = call.parameters.get("page")!!
            val pagesize = call.parameters.get("pagesize")!!
            log.info("pageno: $pageno, pagesize: $pagesize")
            val ships = mongoDataHandler.shipsSortedByFuelAndPaged(pageno.toInt(), pagesize.toInt())
            call.respond(ships!!)
        }

        get("/ships") {
            val fuelmin = call.parameters.get("fuelmin")!!
            log.info("fuelmin $fuelmin")
            val ships = mongoDataHandler.shipsWithMoreFuelThan(fuelmin.toFloat())
            call.respond(ships!!)
        }

        get("/oneship/{spaceshipid}") {
            val shipid = call.parameters.get("spaceshipid")!!
            log.info("shipid $shipid")
            val ship = mongoDataHandler.findOneSpaceShip(shipid)
            call.respond(ship!!)
        }
    }
}

