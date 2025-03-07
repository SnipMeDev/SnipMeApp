package dev.snipme.snipmeapp.infrastructure.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserEntry::class, SnippetEntry::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun snippetDao(): SnippetDao
}
