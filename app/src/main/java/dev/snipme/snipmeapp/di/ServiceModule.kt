package dev.snipme.snipmeapp.di

import androidx.room.Room
import dev.snipme.snipmeapp.infrastructure.local.AppDatabase
import dev.snipme.snipmeapp.infrastructure.local.SnippetDao
import dev.snipme.snipmeapp.infrastructure.local.UserDao
import org.koin.dsl.module
import dev.snipme.snipmeapp.infrastructure.remote.*
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
        ).createFromAsset("app_database.db").build()
    }

    single<UserDao> { get<AppDatabase>().userDao() }
    single<SnippetDao> { get<AppDatabase>().snippetDao() }

}