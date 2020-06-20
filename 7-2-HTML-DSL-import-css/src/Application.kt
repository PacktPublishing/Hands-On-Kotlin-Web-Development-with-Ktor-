package com.example

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.html.*
import kotlinx.html.*
import kotlinx.css.*
import io.ktor.content.*
import io.ktor.http.content.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        get("/htmlexample") {
            call.respondHtml {
                head {
                    link(
                        rel = "stylesheet",
                        href = "https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css",
                        type = "text/css"
                    ) {
                        this.integrity = "sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk"
                        this.attributes.put("crossorigin", "anonymous")
                    }
                }

                body {
                    div(classes = "container") {
                        div(classes = "row") {
                            div(classes = "offset-md-4 col-md-4 order-md-2 mb-4") {
                                h1 { +"My application" }
                                ul {
                                    for (n in 1..10) {
                                        li { +"$n" }
                                    }
                                }
                                button(classes = "btn btn-warning") { +"Test 123" }
                                br(){}
                                br(){}
                                div(classes="alert alert-success"){
                                    this.role="alert"
                                    +"My Ktor and Bootstrap application loaded with success!"
                                }
                            }
                        }
                    }
                   script(type = "javascript", src = "https://code.jquery.com/jquery-3.5.1.slim.min.js") {
                        integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj"
                        this.attributes.put("crossorigin","anonymous")
                    }
                    script(
                        type = "javascript",
                        src = "https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
                    ) {
                        integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
                        this.attributes.put("crossorigin","anonymous")
                    }
                    script(
                        type = "javascript",
                        src = "https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"
                    ) {
                        integrity="sha384-OgVRvuATP1z7JjHLkuOU7Xw704+h835Lr+6QL9UvYjZE3Ipu6Tp75j7Bh/kR0JKI"
                        this.attributes.put("crossorigin","anonymous")
                    }

                }
            }
        }

        get("/styles.css") {
            call.respondCss {
                body {
                    backgroundColor = Color.red
                }
                p {
                    fontSize = 2.em
                }
                rule("p.myclass") {
                    color = Color.blue
                }
            }
        }

        // Static feature. Try to access `/static/ktor_logo.svg`
        static("/static") {
            resources("static")
        }
    }
}

fun FlowOrMetaDataContent.styleCss(builder: CSSBuilder.() -> Unit) {
    style(type = ContentType.Text.CSS.toString()) {
        +CSSBuilder().apply(builder).toString()
    }
}

fun CommonAttributeGroupFacade.style(builder: CSSBuilder.() -> Unit) {
    this.style = CSSBuilder().apply(builder).toString().trim()
}

suspend inline fun ApplicationCall.respondCss(builder: CSSBuilder.() -> Unit) {
    this.respondText(CSSBuilder().apply(builder).toString(), ContentType.Text.CSS)
}
