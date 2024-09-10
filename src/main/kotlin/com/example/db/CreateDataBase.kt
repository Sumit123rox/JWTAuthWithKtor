package com.example.db

import io.ktor.application.*
import org.ktorm.database.Database

fun Application.createDB(database: Database) {

    //Insert
    /*database.insert(Note) { note ->
        set(note.note, "Study Ktor")
    }*/

    //fetch
    /*val notes = database.from(Note).select()

    for (row in notes) {
        println("id: ${row[Note.id]} -> note: ${row[Note.note]}")
    }*/

    //update
    /*database.update(Note) { note ->
        set(note.note, "Learning Ktor")
        where {
            note.id eq 2
        }
    }*/

    //delete
    /*database.delete(Note) { note ->
        note.id eq 5
    }*/

}