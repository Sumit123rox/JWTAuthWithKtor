package com.example

import com.example.db.routing.authRoutes
import com.example.db.routing.notesRoutes
import com.example.utils.TokenManager
import com.typesafe.config.ConfigFactory
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.config.*
import io.ktor.features.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {

    embeddedServer(Netty, port = 8080, host = "0.0.0.0", watchPaths = listOf("classes")) {
        val config = HoconApplicationConfig(ConfigFactory.load())
        val tokenManager = TokenManager(config)

        install(ContentNegotiation) {
            json()
        }

        notesRoutes()

        install(Authentication) {
            jwt {
                verifier(tokenManager.verifyJWTToken())
                realm = config.property("realm").getString()
                validate { jwtCredential ->
                    if (jwtCredential.payload.getClaim("username").asString().isNotEmpty()) {
                        JWTPrincipal(jwtCredential.payload)
                    } else {
                        null
                    }
                }
            }
        }

        authRoutes(tokenManager)

    }.start(wait = true)
}
