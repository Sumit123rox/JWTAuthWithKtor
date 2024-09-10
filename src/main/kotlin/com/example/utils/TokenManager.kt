package com.example.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.example.model.User
import io.ktor.config.*
import java.util.*

class TokenManager(config: HoconApplicationConfig) {

    private val secret = config.property("secret").getString()
    private val issuer = config.property("issuer").getString()
    private val audience = config.property("audience").getString()
    private val expirationDate = System.currentTimeMillis() + 600000

    fun generateJWTToken(user: User): String {

        return JWT.create()
            .withIssuer(issuer)
            .withAudience(audience)
            .withClaim("username", user.username)
            .withClaim("userId", user.id)
            .withExpiresAt(Date(expirationDate))
            .sign(Algorithm.HMAC256(secret))
    }

    fun verifyJWTToken(): JWTVerifier =
        JWT.require(Algorithm.HMAC256(secret))
            .withIssuer(issuer)
            .withAudience(audience)
            .build()
}