package dev.snipme.snipmeapp.di

import androidx.room.Room
import dev.snipme.snipmeapp.infrastructure.local.AppDatabase
import dev.snipme.snipmeapp.infrastructure.local.SnippetDao
import dev.snipme.snipmeapp.infrastructure.local.UserDao
import dev.snipme.snipmeapp.infrastructure.remote.AuthService
import dev.snipme.snipmeapp.infrastructure.remote.LanguageService
import dev.snipme.snipmeapp.infrastructure.remote.ShareService
import dev.snipme.snipmeapp.infrastructure.remote.SnippetService
import dev.snipme.snipmeapp.infrastructure.remote.UserService
import org.koin.dsl.module
import retrofit2.Retrofit

internal val serviceModule = module {
    single<AuthService> { get<Retrofit>().create(AuthService::class.java) }
    single<UserService> { get<Retrofit>().create(UserService::class.java) }
    single<SnippetService> { get<Retrofit>().create(SnippetService::class.java) }
    single<LanguageService> { get<Retrofit>().create(LanguageService::class.java) }
    single<ShareService> { get<Retrofit>().create(ShareService::class.java) }


    single<AppDatabase> {
        Room.databaseBuilder(
            get(), AppDatabase::class.java, "app_database"
        ).build()
    }

    single<UserDao> { get<AppDatabase>().userDao() }
    single<SnippetDao> { get<AppDatabase>().snippetDao() }

}