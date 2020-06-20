package com.example

import io.ktor.application.call
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post

fun Routing.userRoutes(){
    get("/user"){
        call.respondText("User1")
    }

    post("/user"){
        call.respondText("The user has been created")
    }
}