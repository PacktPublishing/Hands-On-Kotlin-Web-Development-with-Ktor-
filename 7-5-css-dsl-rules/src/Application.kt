package com.example

import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.log
import io.ktor.html.respondHtml
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import kotlinx.css.*
import kotlinx.css.properties.*
import kotlinx.html.*
import javax.swing.Spring.scale


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    routing {
        get("/html") {
            call.respondHtml {
                head {
                    link(rel = "stylesheet", href = "/customstyles.css?theme=dark", type = "text/css")
                }
                body {
                    h1 { +"CSS-DSL Example with advanced CSS" }
                    div("colorchange") {
                        +"Hi this is a color changing box"
                    }
                    div("divblue") {
                        +"Hi this is a blue box"
                    }
                    div("divgreen") {
                        +"Hi this is a green box!"
                    }
                    div("divbrown") {
                        +"Hi this is a brown box! The color is made from hex value."
                    }

                }
            }
        }

        get("/customstyles.css") {
            val theme = call.parameters.get("theme")
            log.info("theme: $theme")
            val isdark = theme == "dark"
            call.respondCss {
                body {
                    color = Color.white
                    if (isdark) {
                        backgroundColor = Color.black
                    } else {
                        backgroundColor = Color.gray
                    }
                }

                rule("@media only screen and (max-width: 800px)") {
                    body {
                        backgroundColor = Color.white
                        color = Color.black
                    }
                }

                media("only screen and (max-width: 600px)") {
                    body {
                        backgroundColor = Color.red
                    }
                }

                rule(".colorchange") {
                    animation(
                        name = "swapcolor"
                        , duration = 5.s
                        , iterationCount = IterationCount.infinite
                        , timing = Timing.easeOut
                        , direction = AnimationDirection.alternate
                    )
                    height = 350.px
                }
                rule("@keyframes swapcolor") {

                    rule("0%") {
                        backgroundColor = Color("#0072f5")
                        transform {
                            scale(0.3)
                        }
                    }
                    rule("100%") {
                        backgroundColor = Color("#f58b00")
                        transform {
                            scale(1.0)
                        }
                    }
                }


                rule(".divblue") {
                    color = Color.white
                    backgroundColor = Color.blue
                    minHeight = 200.px
                    fontSize = 1.5.em
                }
                rule(".divgreen") {
                    color = Color.white
                    backgroundColor = Color.green
                    minHeight = 150.px
                    fontSize = 2.em
                }
                rule(".divbrown") {
                    color = Color.white
                    backgroundColor = Color("#BF6600")
                    minHeight = 100.px
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
