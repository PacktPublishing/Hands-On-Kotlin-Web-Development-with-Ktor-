package com.example.ui.login

import com.example.*
import com.example.model.Cart
import com.example.model.DataManagerMongoDB
import com.example.ui.Endpoints
import com.example.ui.books.BookTemplate
import com.example.ui.home.HomeTemplate
import io.ktor.application.call
import io.ktor.html.respondHtmlTemplate
import io.ktor.http.content.PartData
import io.ktor.request.receiveMultipart
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import org.slf4j.LoggerFactory
import java.util.*

data class Session(val username: String, val uniqueID: String = UUID.randomUUID().toString())

fun Route.loginView() {
    get(Endpoints.LOGIN.url) {
        call.respondHtmlTemplate(LoginTemplate(call.sessions.get<Session>())) {
        }
    }
    get(Endpoints.HOME.url) {
        call.respondHtmlTemplate(HomeTemplate(call.sessions.get<Session>())) {}
    }

    get(Endpoints.LOGOUT.url) {
        call.sessions.clear(Constants.COOKIE_NAME.value)
        call.respondHtmlTemplate(LogoutTemplate(call.sessions.get<Session>())) {}
    }

    post(Endpoints.DOLOGIN.url) {
        val log = LoggerFactory.getLogger("LoginView")
        val multipart = call.receiveMultipart()
        var username: String =""
        var password: String =""
        while (true) {
            val part = multipart.readPart() ?: break
            when (part) {
                is PartData.FormItem -> {
                    log.info("FormItem: ${part.name} = ${part.value}")
                    if (part.name == "username")
                        username = part.value
                    if (part.name == "password")
                        password = part.value
                }
                is PartData.FileItem -> {
                    log.info("FileItem: ${part.name} -> ${part.originalFileName} of ${part.contentType}")
                }
            }
            part.dispose()
        }
        if (SecurityHandler().isValid(username, password)) {
            call.sessions.set(Constants.COOKIE_NAME.value, Session(username))
            call.respondHtmlTemplate(
                BookTemplate(call.sessions.get<Session>(),
                    DataManagerMongoDB.INSTANCE.allBooks())
            ) {
                searchfilter {
                    +"You are logged in as $username and a cookie has been created"
                }
            }
        }
        else
            call.respondHtmlTemplate(LoginTemplate(call.sessions.get<Session>())){
                greeting{
                    +"Username or password was invalid... Try again."
                }
            }
    }
}
