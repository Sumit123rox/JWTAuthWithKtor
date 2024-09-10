package com.example.plugins

import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*

fun Application.configureRouting() {

    routing {
        get("/") {
                call.respond(HttpStatusCode.OK, "")
            }

        get("/customer/{id}") {
            val id = call.parameters["id"]

        }
    }
}
