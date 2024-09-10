package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthUser(
    val email: String,
    val password: String
)
