package com.example

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.html.*
import kotlinx.html.*
import kotlinx.css.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    routing {

        get("/html") {
            call.respondHtml {
                head{
                    link(rel="stylesheet", href="/customstyles.css",type="text/css")
                }
                body {
                    h1 { +"CSS-DSL Example" }
                    div("divblue"){
                        +"Hi this is a blue box"
                    }
                    div("divgreen"){
                        +"Hi this is a green box!"
                    }
                    div("divbrown"){
                        +"Hi this is a brown box! The color is made from hex value."
                    }
                }
            }
        }

        get("/customstyles.css") {
            call.respondCss {
                body {
                    backgroundColor = Color.gray
                }
                rule(".divblue") {
                    color = Color.white
                    backgroundColor = Color.blue
                    minHeight=200.px
                    fontSize = 1.5.em
                }
                rule(".divgreen") {
                    color = Color.white
                    backgroundColor = Color.green
                    minHeight=150.px
                    fontSize = 2.em
                }
                rule(".divbrown") {
                    color = Color.white
                    backgroundColor = Color("#BF6600")
                    minHeight=100.px
                    fontSize = 2.5.em
                }
            }
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
