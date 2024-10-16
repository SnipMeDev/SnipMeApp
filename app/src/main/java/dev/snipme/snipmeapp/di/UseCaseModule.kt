package dev.snipme.snipmeapp.di

import org.koin.dsl.module
import dev.snipme.snipmeapp.domain.auth.*
import dev.snipme.snipmeapp.domain.clipboard.AddToClipboardUseCase
import dev.snipme.snipmeapp.domain.clipboard.GetFromClipboardUseCase
import dev.snipme.snipmeapp.domain.language.GetLanguagesUseCase
import dev.snipme.snipmeapp.domain.network.CheckNetworkAvailableUseCase
import dev.snipme.snipmeapp.domain.reaction.GetTargetUserReactionUseCase
import dev.snipme.snipmeapp.domain.reaction.SetUserReactionUseCase
import dev.snipme.snipmeapp.domain.share.ShareSnippetCodeUseCase
import dev.snipme.snipmeapp.domain.snippet.*
import dev.snipme.snipmeapp.domain.filter.FilterSnippetsByLanguageUseCase
import dev.snipme.snipmeapp.domain.filter.FilterSnippetsByScopeUseCase
import dev.snipme.snipmeapp.domain.filter.GetLanguageFiltersUseCase
import dev.snipme.snipmeapp.domain.filter.UpdateSnippetFiltersLanguageUseCase
import dev.snipme.snipmeapp.domain.snippets.GetSnippetsUseCase
import dev.snipme.snipmeapp.domain.snippets.HasMoreSnippetPagesUseCase
import dev.snipme.snipmeapp.domain.user.GetSingleUserUseCase

internal val useCaseModule = module {
    // Base
    factory { CheckNetworkAvailableUseCase(get()) }
    // Auth
    factory { IdentifyUserUseCase(get(), get()) }
    factory { InitialLoginUseCase(get()) }
    factory { LoginUseCase(get(), get()) }
    factory { RegisterUseCase(get(), get()) }
    factory { LogoutUserUseCase(get()) }
    factory { AuthorizationUseCase(get()) }
    // User
    factory { GetSingleUserUseCase(get(), get(), get()) }
    // Snippet
    factory { GetSnippetsUseCase(get(), get(), get()) }
    factory { GetSingleSnippetUseCase(get(), get(), get()) }
    factory { HasMoreSnippetPagesUseCase(get(), get(), get()) }
    factory { CreateSnippetUseCase(get(), get(), get()) }
    factory { UpdateSnippetUseCase(get(), get(), get()) }
    factory { ObserveUpdatedSnippetPageUseCase(get()) }
    factory { ObserveSnippetUpdatesUseCase(get()) }
    factory { GetTargetUserReactionUseCase() }
    factory { SetUserReactionUseCase(get(), get(), get()) }
    factory { DeleteSnippetUseCase(get()) }
    // Language
    factory { GetLanguagesUseCase(get(), get(), get()) }
    // Share
    factory { ShareSnippetCodeUseCase(get()) }
    // Clipboard
    single { AddToClipboardUseCase(get()) }
    factory { GetFromClipboardUseCase(get()) }
    // Save
    factory { SaveSnippetUseCase(get()) }
    // Filter
    factory { GetLanguageFiltersUseCase() }
    factory { FilterSnippetsByLanguageUseCase() }
    factory { FilterSnippetsByScopeUseCase() }
    factory { UpdateSnippetFiltersLanguageUseCase() }
}

internal val interactorModule = module {
    factory { LoginInteractor(get(), get(), get()) }
    factory { EditInteractor(get(), get(), get(), get(), get()) }
}