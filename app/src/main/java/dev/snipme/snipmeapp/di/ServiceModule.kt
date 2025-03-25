package dev.snipme.snipmeapp.di

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
}