package com.example.api

import com.example.model.DataManagerMongoDB
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route

@Location("/api/book/list")
data class BookListLocation(val sortby: String, val asc: Boolean)

fun Route.booksapi(){
    authenticate("bookStoreAuth") {
        get<BookListLocation>() {
            call.respond(DataManagerMongoDB.INSTANCE.sortedBooks(it.sortby, it.asc))
        }
    }
}