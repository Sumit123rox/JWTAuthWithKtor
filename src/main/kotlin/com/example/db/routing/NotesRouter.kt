package com.example.db.routing

import com.example.db.DBConnection
import com.example.db.entity.NoteEntity
import com.example.model.Note
import com.example.model.NoteRequest
import com.example.model.NoteResponse
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.ktorm.dsl.*

fun Application.notesRoutes() {

    val db = DBConnection.db

    routing {
        //get notes from note table
        get("/notes") {
            val notes = db.from(NoteEntity).select().map { note ->
                Note(note[NoteEntity.id] ?: -1, note[NoteEntity.note] ?: "")
            }

            call.respond(notes)
        }

        //add note in note table
        post("/notes") {
            val request = call.receive<NoteRequest>()
            val result = db.insert(NoteEntity) { note ->
                set(note.note, request.note)
            }

            if (result == 1) {
                // Send successfully response to the client
                call.respond(
                    HttpStatusCode.OK, NoteResponse(
                        data = "Values has been successfully inserted.",
                        success = true
                    )
                )
            } else {
                // Send failure response to the client
                call.respond(
                    HttpStatusCode.BadRequest, NoteResponse(
                        data = "Failed to inserted values.",
                        success = false
                    )
                )
            }
        }

        //get particular note from note
        get("/notes/{id}") {
            val id = call.parameters["id"]?.toInt() ?: -1

            val note = db.from(NoteEntity)
                .select()
                .where {
                    NoteEntity.id eq id
                }
                .map { note ->
                    Note(note[NoteEntity.id] ?: -1, note[NoteEntity.note] ?: "")
                }.firstOrNull()

            if (note != null) {
                call.respond(
                    HttpStatusCode.OK, NoteResponse(
                        success = true,
                        data = note
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.NotFound, NoteResponse(
                        success = false,
                        data = "Could n't found note with id: $id"
                    )
                )
            }
        }

        put("/notes/{id}") {
            val id = call.parameters["id"]?.toInt() ?: -1
            val request = call.receive<NoteRequest>()

            val result = db.update(NoteEntity) { note ->
                set(note.note, request.note)
                where {
                    note.id eq id
                }
            }

            if (result == 1) {
                // Send successfully response to the client
                call.respond(
                    HttpStatusCode.OK, NoteResponse(
                        data = "Note has been successfully updated.",
                        success = true
                    )
                )
            } else {
                // Send failure response to the client
                call.respond(
                    HttpStatusCode.BadRequest, NoteResponse(
                        data = "Failed to update Note.",
                        success = false
                    )
                )
            }
        }

        delete("/notes/{id}") {
            val id = call.parameters["id"]?.toInt() ?: -1

            val result = db.delete(NoteEntity) {
                it.id eq id
            }

            if (result == 1) {
                // Send successfully response to the client
                call.respond(
                    HttpStatusCode.OK, NoteResponse(
                        data = "Note has been successfully deleted.",
                        success = true
                    )
                )
            } else {
                // Send failure response to the client
                call.respond(
                    HttpStatusCode.BadRequest, NoteResponse(
                        data = "Failed to delete Note.",
                        success = false
                    )
                )
            }
        }
    }
}