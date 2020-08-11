package com.kotlin.notesapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Note (
    val tittle: String,
    val note: String
):Serializable

{
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}