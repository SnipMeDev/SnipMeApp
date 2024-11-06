package dev.snipme.snipmeapp.di

import org.koin.dsl.module
import dev.snipme.snipmeapp.domain.repository.auth.AuthRepository
import dev.snipme.snipmeapp.domain.repository.auth.AuthRepositoryReal
import dev.snipme.snipmeapp.domain.repository.language.LanguageRepository
import dev.snipme.snipmeapp.domain.repository.language.LanguageRepositoryReal
import dev.snipme.snipmeapp.domain.repository.networkstate.NetworkStateRepository
import dev.snipme.snipmeapp.domain.repository.networkstate.NetworkStateRepositoryReal
import dev.snipme.snipmeapp.domain.repository.snippet.SnippetRepository
import dev.snipme.snipmeapp.domain.repository.snippet.SnippetRepositoryReal
import dev.snipme.snipmeapp.domain.repository.user.UserRepository
import dev.snipme.snipmeapp.domain.repository.user.UserRepositoryReal

internal val repositoryModule = module {
    single<NetworkStateRepository> { NetworkStateRepositoryReal() }
    single<AuthRepository> { AuthRepositoryReal(get(), get(), get()) }
    single<UserRepository> { UserRepositoryReal(get(), get()) }
    single<SnippetRepository> { SnippetRepositoryReal(get(), get(), get()) }
    single<LanguageRepository> { LanguageRepositoryReal(get(), get(), get()) }
}