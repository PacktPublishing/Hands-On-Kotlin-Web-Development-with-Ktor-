package com.example

import com.example.model.DataManagerMongoDB
import com.example.ui.Endpoints
import com.example.ui.login.Session
import io.ktor.html.PlaceholderList
import io.ktor.html.Template
import io.ktor.html.each
import io.ktor.html.insert
import kotlinx.html.*

class NavigationTemplate(val session: Session?) : Template<FlowContent> {
    val menuitems = PlaceholderList<UL, FlowContent>()

    override fun FlowContent.apply() {
        div {
            nav(classes = "navbar navbar-expand-md navbar-dark bg-dark ") {
                a(classes = "navbar-brand", href = "#") { +"My Bookstore" }
                button(
                    classes = "navbar-toggler",
                    type = ButtonType.button
                ) {
                    this.attributes.put("data-toggle", "collapse")
                    this.attributes.put("data-target", "#navbarsExampleDefault")
                    this.attributes.put("aria-controls", "navbarsExampleDefault")
                    this.attributes.put("aria-expanded", "false")
                    this.attributes.put("aria-label", "Toggle navigation")
                    span(classes = "navbar-toggler-icon") {}
                }
                div(classes = "collapse navbar-collapse") {
                    this.id = "navbarsExampleDefault"
                    ul(classes = "navbar-nav mr-auto") {
                        each(menuitems) {
                            li {
                                    insert(it)
                            }
                        }
                    }
                }
                div(classes=""){
                    if (session != null){
                        val cartForUser = DataManagerMongoDB.INSTANCE.cartForUser(session)
                        form(action=Endpoints.CART.url){
                            button(classes="btn btn-danger", type=ButtonType.submit){
                                +"Items in cart: ${cartForUser?.qtyTotal}, total price: ${cartForUser?.sum}"
                            }
                        }
                    }
                }
            }
        }
    }
}