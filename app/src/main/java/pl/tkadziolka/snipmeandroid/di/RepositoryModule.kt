package pl.tkadziolka.snipmeandroid.di

import org.koin.dsl.module
import pl.tkadziolka.snipmeandroid.domain.repository.auth.AuthRepository
import pl.tkadziolka.snipmeandroid.domain.repository.auth.AuthRepositoryReal
import pl.tkadziolka.snipmeandroid.domain.repository.language.LanguageRepository
import pl.tkadziolka.snipmeandroid.domain.repository.language.LanguageRepositoryReal
import pl.tkadziolka.snipmeandroid.domain.repository.networkstate.NetworkStateRepository
import pl.tkadziolka.snipmeandroid.domain.repository.networkstate.NetworkStateRepositoryReal
import pl.tkadziolka.snipmeandroid.domain.repository.snippet.SnippetRepository
import pl.tkadziolka.snipmeandroid.domain.repository.snippet.SnippetRepositoryReal
import pl.tkadziolka.snipmeandroid.domain.repository.user.UserRepository
import pl.tkadziolka.snipmeandroid.domain.repository.user.UserRepositoryReal

internal val repositoryModule = module {
    single<NetworkStateRepository> { NetworkStateRepositoryReal() }
    single<AuthRepository> { AuthRepositoryReal(get(), get(), get()) }
    single<UserRepository> { UserRepositoryReal(get(), get()) }
    single<SnippetRepository> { SnippetRepositoryReal(get(), get(), get()) }
    single<LanguageRepository> { LanguageRepositoryReal(get(), get(), get()) }
}