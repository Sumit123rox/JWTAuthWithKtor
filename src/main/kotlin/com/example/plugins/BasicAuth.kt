package com.example.plugins

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.basicAuth() {

    install(Authentication) {
        basic("auth-basic") {
            realm = "Access to '/' path"
            validate { credentials ->
                if (credentials.name == "sumit" && credentials.password == "Sumit123") {
                    UserIdPrincipal(credentials.name)
                } else
                    null
            }
        }
    }

    routing {
        authenticate("auth-basic") {
            get("/") {
                call.respondText("Hello, ${call.principal<UserIdPrincipal>()?.name}!")
            }
        }
    }
}