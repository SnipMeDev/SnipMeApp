package dev.snipme.snipmeapp.infrastructure.local

import androidx.room.Embedded
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
    val ownerId: Int,
    val language: String,
    val numberOfLikes: Int,
    val numberOfDislikes: Int,
    val userReaction: String
)


data class SnippetWithOwner(
    @Embedded val snippet: SnippetEntry,
    val ownerName: String,
    val isOwner: Boolean
)
