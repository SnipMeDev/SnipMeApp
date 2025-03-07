package dev.snipme.snipmeapp.infrastructure.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "snippets")
data class SnippetEntry(
    @PrimaryKey(true) val id: Long = 0,
    val title: String,
    val code: String,
    val createdAt: String,
    val modifiedAt: String,
    val visibility: String,
    val language: String,
    val favorite: Boolean,
)
