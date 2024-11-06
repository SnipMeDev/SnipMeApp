package dev.snipme.snipmeapp.infrastructure.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntry(
    @PrimaryKey(true) val id: Int = 0,
    val email: String,
    val password: String,
    val login: String,
    val photo: String
)