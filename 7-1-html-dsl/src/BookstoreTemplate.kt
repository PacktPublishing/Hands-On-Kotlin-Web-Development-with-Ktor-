package com.example

import io.ktor.html.Placeholder
import io.ktor.html.Template
import io.ktor.html.insert
import kotlinx.html.HTML
import kotlinx.html.*

class BookstoreTemplate: Template<HTML> {
    val bookOnSale = Placeholder<FlowContent>()
    val bookRecommended = Placeholder<FlowContent>()

    override fun HTML.apply() {
        head {
            title { +"Bookstore Application"}
        }
        body{
            h1{
                +"Welcome to my bookstore"
            }
            div{
                h1{
                    +"On sale!"
                }
                h2{
                    insert(bookOnSale)
                }
            }
            div{
                h1{
                    +"Recommended for you"
                }
                h2{
                    insert(bookRecommended)
                }
            }
        }
    }
}