package dev.snipme.snipmeapp.di

import androidx.room.Room
import dev.snipme.snipmeapp.infrastructure.local.AppDatabase
import dev.snipme.snipmeapp.infrastructure.local.AuthPreferences
import dev.snipme.snipmeapp.infrastructure.local.SnippetDao
import dev.snipme.snipmeapp.infrastructure.local.UserDao
import dev.snipme.snipmeapp.util.PreferencesUtil
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val storageModule = module {
    single { PreferencesUtil(androidContext()) }
    single { AuthPreferences(get()) }

    single<AppDatabase> {
        Room.databaseBuilder(get(), AppDatabase::class.java, "app_database").build()
    }

    single<UserDao> { get<AppDatabase>().userDao() }
    single<SnippetDao> { get<AppDatabase>().snippetDao() }
}