package dev.snipme.snipmeapp.infrastructure.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "snippets")
data class SnippetEntry(
    @PrimaryKey(true) val id: Long = 0,
    val title: String,
    val code: String,
    val createdAt: Date,
    val modifiedAt: Date,
    val visibility: String,
    val language: String,
    val favorite: Boolean,
)
