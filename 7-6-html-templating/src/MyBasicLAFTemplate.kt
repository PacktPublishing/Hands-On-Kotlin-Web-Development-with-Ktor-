package com.example

import io.ktor.html.Placeholder
import io.ktor.html.Template
import io.ktor.html.TemplatePlaceholder
import io.ktor.html.insert
import kotlinx.css.script
import kotlinx.html.*

class MyBasicLAFTemplate : Template<HTML> {
    val content = Placeholder<HtmlBlockTag>()
    val menu = TemplatePlaceholder<NavigationTemplate>()

    override fun HTML.apply() {
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
            insert(NavigationTemplate(), menu)

            div(classes = "container") {
                div(classes = "row") {
                    div(classes = "col-md-6 offset-md-3") {
                        insert(content)
                    }
                }
            }
            script(type = "javascript", src = "https://code.jquery.com/jquery-3.5.1.slim.min.js") {
                integrity = "sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj"
                this.attributes.put("crossorigin", "anonymous")
            }
            script(
                type = "javascript",
                src = "https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
            ) {
                integrity = "sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
                this.attributes.put("crossorigin", "anonymous")
            }
            script(
                type = "javascript",
                src = "https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"
            ) {
                integrity = "sha384-OgVRvuATP1z7JjHLkuOU7Xw704+h835Lr+6QL9UvYjZE3Ipu6Tp75j7Bh/kR0JKI"
                this.attributes.put("crossorigin", "anonymous")
            }
        }

    }

}