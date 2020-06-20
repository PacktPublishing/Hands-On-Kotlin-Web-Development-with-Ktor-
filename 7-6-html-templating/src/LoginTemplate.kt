package com.example

import io.ktor.html.Placeholder
import io.ktor.html.Template
import io.ktor.html.insert
import kotlinx.html.*

class LoginTemplate(val basicTemplate: MyBasicLAFTemplate = MyBasicLAFTemplate()) : Template<HTML> {
    val greeting = Placeholder<FlowContent>()
    override fun HTML.apply() {

        insert(basicTemplate) {
            menu {
                menuitems { +"Home" }
                menuitems { +"Privacy Policy" }
                menuitems { +"Continue without login" }
            }
            content {
                div(classes = "mt-2") {
                    h2() {
                        insert(greeting)
                    }
                }

                div(classes = "mb-3") {
                    input(type = InputType.text, classes = "form-control") {
                        this.placeholder = "Type in your username here..."
                        this.attributes.put("aria-label", "Username")
                        this.attributes.put("aria-describedby", "basic-addon1")
                    }
                }
                div(classes = "mb-3") {
                    input(type = InputType.password, classes = "form-control") {
                        this.placeholder = "Type in your password here..."
                        this.attributes.put("aria-label", "Password")
                        this.attributes.put("aria-describedby", "basic-addon1")
                    }
                }

                div(classes = "mb-3") {
                    button(classes = "btn btn-primary", type = ButtonType.button) {
                        +"Try to login"
                    }
                }
            }
        }
    }
}