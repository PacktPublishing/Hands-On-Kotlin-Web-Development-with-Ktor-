package com.example.ui.books

import com.example.model.Book
import com.example.GeneralViewTemplate
import com.example.ui.Endpoints
import com.example.ui.login.Session
import io.ktor.html.Placeholder
import io.ktor.html.Template
import io.ktor.html.insert
import kotlinx.html.*

class BookTemplate(val session: Session?, val books: List<Book>) : Template<HTML> {
    val basicTemplate: GeneralViewTemplate = GeneralViewTemplate(session)
    val searchfilter = Placeholder<FlowContent>()
    override fun HTML.apply() {
        insert(basicTemplate) {

            content {
                div(classes = "mt-2") {
                    h2() {
                        +"Books available"
                    }
                    div {
                        insert(searchfilter)
                    }
                    form(
                        method = FormMethod.post,
                        encType = FormEncType.multipartFormData,
                        action = Endpoints.DOBOOKSEARCH.url
                    ) {
                        div(classes = "row mb-3 mt-3") {
                            div(classes = "md-6") {
                                input(type = InputType.text, classes = "form-control", name = "search") {
                                    this.placeholder = "Search for book"
                                    this.attributes.put("aria-label", "Search")
                                    this.attributes.put("aria-describedby", "basic-addon1")
                                }
                            }
                            div(classes = "md-5 offset-md-1") {

                                button(classes = "btn btn-primary", type = ButtonType.submit) {
                                    +"Search"
                                }


                            }
                        }
                    }

                    table(classes = "table table-striped") {
                        thead {
                            tr {
                                th(scope = ThScope.col) { +"Id" }
                                th(scope = ThScope.col) { +"Title" }
                                th(scope = ThScope.col) { +"Author" }
                                th(scope = ThScope.col) { +"Price" }
                                th(scope = ThScope.col) { +"" }
                            }
                        }
                        tbody {
                            books.forEach() {
                                tr {
                                    td { +"${it.id}" }
                                    td { +"${it.title}" }
                                    td { +"${it.author}" }
                                    td { +"${it.price}" }
                                    td {
                                        form(
                                            method = FormMethod.post,
                                            encType = FormEncType.multipartFormData,
                                            action = Endpoints.DOADDTOCART.url
                                        ) {
                                            button(classes = "btn btn-success", type = ButtonType.submit) {
                                                +"Add to cart"
                                            }
                                            input(type = InputType.hidden, name = "bookid") {
                                                value="${it.id}"
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
    }
}