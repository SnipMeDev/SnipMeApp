package pl.tkadziolka.snipmeandroid.di

import org.koin.dsl.module
import pl.tkadziolka.snipmeandroid.infrastructure.remote.*
import retrofit2.Retrofit

internal val serviceModule = module {
    single<AuthService> { get<Retrofit>().create(AuthService::class.java) }
    single<UserService> { get<Retrofit>().create(UserService::class.java) }
    single<SnippetService> { get<Retrofit>().create(SnippetService::class.java) }
    single<LanguageService> { get<Retrofit>().create(LanguageService::class.java) }
    single<ShareService> { get<Retrofit>().create(ShareService::class.java) }
}