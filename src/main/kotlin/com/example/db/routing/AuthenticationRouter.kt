package com.example.db.routing

import com.example.db.DBConnection
import com.example.db.entity.UserEntity
import com.example.model.NoteResponse
import com.example.model.User
import com.example.model.UserCredentials
import com.example.utils.TokenManager
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.ktorm.dsl.*
import org.mindrot.jbcrypt.BCrypt

fun Application.authRoutes(tokenManager: TokenManager) {
    val db = DBConnection.db

    routing {
        post("/register") {
            val userCredentials = call.receive<UserCredentials>()

            //Check if user credentials are valid or not
            if (!userCredentials.isValidCredentials()) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    NoteResponse(
                        success = false,
                        data = "Username must be greater than or equal to 3 and password must be greater than  or equal to 6"
                    )
                )
                return@post
            }

            val username = userCredentials.username.lowercase()
            val password = userCredentials.hashedPassword()

            //Check if username already exists or not
            val user = db.from(UserEntity).select()
                .where { UserEntity.username eq username }
                .map { it[UserEntity.username] }
                .firstOrNull()

            if (user != null) {
                call.respond(
                    HttpStatusCode.BadRequest, NoteResponse(
                        success = false,
                        data = "User is already exists, please try with a different username"
                    )
                )
                return@post
            }

            val result = db.insert(UserEntity) { userEntity ->
                set(userEntity.username, username)
                set(userEntity.password, password)
            }

            call.respond(
                HttpStatusCode.Created, NoteResponse(
                    success = true,
                    data = "User successfully registered."
                )
            )
        }

        post("/login") {
            val userCredentials = call.receive<UserCredentials>()

            //Check if user credentials are valid or not
            if (!userCredentials.isValidCredentials()) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    NoteResponse(
                        success = false,
                        data = "Username must be greater than or equal to 3 and password must be greater than  or equal to 6"
                    )
                )
                return@post
            }

            val username = userCredentials.username.lowercase()
            val password = userCredentials.password

            //Check if username already exists or not
            val user = db.from(UserEntity).select()
                .where { UserEntity.username eq username }
                .map { userRow ->
                    User(
                        userRow[UserEntity.id]?.toInt() ?: -1,
                        userRow[UserEntity.username] ?: "",
                        userRow[UserEntity.password] ?: "",
                    )
                }
                .firstOrNull()

            if (user == null) {
                call.respond(
                    HttpStatusCode.BadRequest, NoteResponse(
                        success = false,
                        data = "Invalid username and password"
                    )
                )
                return@post
            }

            val doesPasswordMatch = BCrypt.checkpw(password, user.password)
            if (!doesPasswordMatch) {
                call.respond(
                    HttpStatusCode.BadRequest, NoteResponse(
                        success = false,
                        data = "Invalid password"
                    )
                )
            }

            val token = tokenManager.generateJWTToken(user)
            call.respond(
                HttpStatusCode.OK, NoteResponse(
                    success = true,
                    data = token
                )
            )
        }

        authenticate {
            get("/me") {
                val jwtPrincipal = call.principal<JWTPrincipal>()
                val username = jwtPrincipal?.payload?.getClaim("username")?.asString()
                val userId = jwtPrincipal?.payload?.getClaim("userId")?.asInt()

                call.respond("Hello, $username with id: $userId")
            }
        }
    }
}