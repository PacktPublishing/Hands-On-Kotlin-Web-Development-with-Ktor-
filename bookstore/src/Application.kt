package com.example

import com.example.ui.books.books
import com.example.ui.cart.cart
import com.example.ui.checkout.receipt
import com.example.ui.login.Session
import com.example.ui.login.loginView
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.*
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.locations.Locations
import io.ktor.request.path
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import org.slf4j.event.Level

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(Sessions) {
        cookie<Session>(Constants.COOKIE_NAME.value)
    }

    install(StatusPages) {
        exception<Throwable> { cause ->
            call.respond(HttpStatusCode.InternalServerError)
            throw cause
        }
    }

    install(Locations) {
    }

    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }
    val users = listOf<String>("shopper1", "shopper2", "shopper3")
    val admins = listOf<String>("admin")
    install(Authentication) {
        basic("bookStoreAuth") {
            realm = "Book store"
            validate {
                if
                        ((users.contains(it.name) || admins.contains(it.name))
                    && it.password == "password"
                ) UserIdPrincipal(it.name)
                else null
            }
        }
    }

    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }

    val client = HttpClient(Apache) {
    }

    routing {
        books()
        cart()
        receipt()
        loginView()
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }



        authenticate("bookStoreAuth") {
            get("/api/tryauth") {
                val principal = call.principal<UserIdPrincipal>()!!
                call.respondText("Hello ${principal.name}")
            }
        }

    }
}


