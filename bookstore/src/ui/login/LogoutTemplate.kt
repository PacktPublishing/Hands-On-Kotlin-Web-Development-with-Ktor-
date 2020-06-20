package com.example.ui.login

import com.example.GeneralViewTemplate
import com.example.ui.Endpoints
import io.ktor.html.Placeholder
import io.ktor.html.Template
import io.ktor.html.insert
import kotlinx.html.*
import java.awt.Event.HOME

class LogoutTemplate(val session: Session?) : Template<HTML> {
    val basicTemplate: GeneralViewTemplate = GeneralViewTemplate(session)
    override fun HTML.apply() {
        insert(basicTemplate) {
            content {
                div(classes = "mt-2") {
                    h2() {
                        +"You have been logged out!"
                    }
                }
            }
        }
    }
}