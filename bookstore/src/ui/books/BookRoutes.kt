package com.example.ui.books

import com.example.model.DataManagerMongoDB
import com.example.ui.Endpoints
import com.example.ui.login.Session
import io.ktor.application.call
import io.ktor.html.respondHtmlTemplate
import io.ktor.http.content.PartData
import io.ktor.request.receiveMultipart
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import kotlinx.html.i
import org.slf4j.LoggerFactory

fun Route.books(){
    get(Endpoints.BOOKS.url){
        call.respondHtmlTemplate(BookTemplate(call.sessions.get<Session>(),
            DataManagerMongoDB.INSTANCE.allBooks())){
        }
    }
    post(Endpoints.DOBOOKSEARCH.url){
        val log = LoggerFactory.getLogger("LoginView")
        val multipart = call.receiveMultipart()
        var search: String =""
        while (true) {
            val part = multipart.readPart() ?: break
            when (part) {
                is PartData.FormItem -> {
                    log.info("FormItem: ${part.name} = ${part.value}")
                    if (part.name == "search")
                        search = part.value
                }
            }
            part.dispose()
        }
        val searchBooks = DataManagerMongoDB.INSTANCE.searchBooks(search)
        call.respondHtmlTemplate(BookTemplate(call.sessions.get<Session>(), searchBooks)){
            searchfilter{
                i{
                    +"Search filter: $search"
                }
            }
        }
    }

}