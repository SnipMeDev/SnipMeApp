package dev.snipme.snipmeapp.infrastructure.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntry(
    @PrimaryKey val email: String,
    val password: String
)