package dev.snipme.snipmeapp.infrastructure.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.snipme.snipmeapp.bridge.Bridge.Snippet

@Entity(
    tableName = "reactions",
    foreignKeys = [
        ForeignKey(
            entity = UserEntry::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("userId"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = SnippetEntry::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("snippetId"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
    ],
    indices = [Index(value = ["userId", "snippetId"], unique = true)]
)
data class ReactionEntry(
    @PrimaryKey(true) val id: Int = 0,
    val userId: Int,
    val snippetId: Int,
    val reaction: Short // 0 dislike, 1 none, 2 like
)