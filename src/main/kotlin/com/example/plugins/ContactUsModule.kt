package com.example.plugins

import com.example.model.AuthUser
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.Serializable
import java.io.File

fun Application.contactUsModule() {


    routing {

        get("/iphones/{page}") {
            /*with(call.request) {
                println("uri: $uri")
                println("header names: ${headers.names()}")
                println("host: ${local.host}")
                println("port: ${local.port}")
                println("developmentMode: ${pipeline.developmentMode}")
                println("pipelineItems: ${pipeline.items}")
                println("method: ${local.method}")
                println("scheme: ${local.scheme}")
                println("localUri: ${local.uri}")
                println("version: ${local.version}")
                println("remoteHost: ${local.remoteHost}")
                println("rawCookies: ${cookies.rawCookies}")
            }*/

            val pageNumber = call.parameters["page"]
            call.respondText("You are on page $pageNumber")
        }

        post("/login") {
            val userInfo = call.receive<AuthUser>()
            println(userInfo)
            call.respondText("Everything is working fine.")
        }

        get("/object") {
            val responseObject = UserInfo("Sumit", "Sumit123")
            call.respond(responseObject)
        }

        get("/headers") {
            call.response.headers.append("server-name", "KtorServer")
            call.respondText("Header Attached")
        }

        get("/fileDownload") {
            val nameOfFile = fetchTheFile().random()
            val file = File("rabbits/$nameOfFile")
            call.response.header(
                HttpHeaders.ContentDisposition,
                ContentDisposition.Attachment.withParameter(
                    ContentDisposition.Parameters.FileName,
                    nameOfFile
                ).toString()
            )

            call.respondFile(file)
        }

        get("/openFile") {
            val nameOfFile = fetchTheFile().random()
            val file = File("rabbits/$nameOfFile")
            call.response.header(
                HttpHeaders.ContentDisposition,
                ContentDisposition.Inline.withParameter(
                    ContentDisposition.Parameters.FileName,
                    nameOfFile
                ).toString()
            )

            call.respondFile(file)
        }
    }
}

fun fetchTheFile(): List<String> = arrayListOf(
    "rabbit1.jpg",
    "rabbit2.jpg",
    "rabbit3.jpg",
    "rabbit4.jpg",
    "rabbit5.jpg"
)

@Serializable
data class UserInfo(
    val name: String,
    val password: String
)