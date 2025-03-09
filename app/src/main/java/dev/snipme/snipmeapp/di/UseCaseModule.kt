package dev.snipme.snipmeapp.di

import dev.snipme.snipmeapp.domain.auth.AuthorizationUseCase
import dev.snipme.snipmeapp.domain.auth.IdentifyUserUseCase
import dev.snipme.snipmeapp.domain.auth.InitialLoginUseCase
import dev.snipme.snipmeapp.domain.auth.LoginInteractor
import dev.snipme.snipmeapp.domain.auth.LoginUseCase
import dev.snipme.snipmeapp.domain.auth.LogoutUserUseCase
import dev.snipme.snipmeapp.domain.auth.RegisterUseCase
import dev.snipme.snipmeapp.domain.clipboard.AddToClipboardUseCase
import dev.snipme.snipmeapp.domain.clipboard.GetFromClipboardUseCase
import dev.snipme.snipmeapp.domain.favorite.SetFavoriteSnippet
import dev.snipme.snipmeapp.domain.filter.FilterSnippetsByLanguageUseCase
import dev.snipme.snipmeapp.domain.filter.FilterSnippetsByScopeUseCase
import dev.snipme.snipmeapp.domain.filter.GetLanguageFiltersUseCase
import dev.snipme.snipmeapp.domain.filter.UpdateSnippetFiltersLanguageUseCase
import dev.snipme.snipmeapp.domain.language.GetLanguagesUseCase
import dev.snipme.snipmeapp.domain.network.CheckNetworkAvailableUseCase
import dev.snipme.snipmeapp.domain.share.ShareSnippetUseCase
import dev.snipme.snipmeapp.domain.snippet.CreateSnippetUseCase
import dev.snipme.snipmeapp.domain.snippet.DeleteSnippetUseCase
import dev.snipme.snipmeapp.domain.snippet.EditInteractor
import dev.snipme.snipmeapp.domain.snippet.GetSingleSnippetUseCase
import dev.snipme.snipmeapp.domain.snippet.ObserveSnippetUpdatesUseCase
import dev.snipme.snipmeapp.domain.snippet.ObserveUpdatedSnippetPageUseCase
import dev.snipme.snipmeapp.domain.snippet.SaveSnippetUseCase
import dev.snipme.snipmeapp.domain.snippet.UpdateSnippetUseCase
import dev.snipme.snipmeapp.domain.snippets.GetDemoSnippetsSetupStatusUseCase
import dev.snipme.snipmeapp.domain.snippets.GetSnippetsUseCase
import dev.snipme.snipmeapp.domain.snippets.HasMoreSnippetPagesUseCase
import dev.snipme.snipmeapp.domain.snippets.SetupDemoSnippetsUseCase
import dev.snipme.snipmeapp.domain.user.GetSingleUserUseCase
import org.koin.dsl.module

internal val useCaseModule = module {
    // Base
    factory { CheckNetworkAvailableUseCase(get()) }
    // Auth
    factory { IdentifyUserUseCase(get()) }
    factory { InitialLoginUseCase(get()) }
    factory { LoginUseCase(get()) }
    factory { RegisterUseCase(get(), get()) }
    factory { LogoutUserUseCase(get()) }
    factory { AuthorizationUseCase(get()) }
    // User
    factory { GetSingleUserUseCase(get(), get(), get(), get()) }
    // Snippet
    factory { GetSnippetsUseCase(get(), get(), get()) }
    factory { GetSingleSnippetUseCase(get(), get(), get()) }
    factory { HasMoreSnippetPagesUseCase(get(), get(), get()) }
    factory { CreateSnippetUseCase(get(), get(), get()) }
    factory { UpdateSnippetUseCase(get(), get(), get()) }
    factory { ObserveUpdatedSnippetPageUseCase(get()) }
    factory { ObserveSnippetUpdatesUseCase(get()) }
    factory { SetFavoriteSnippet(get()) }
    factory { DeleteSnippetUseCase(get()) }
    factory { GetDemoSnippetsSetupStatusUseCase(get()) }
    factory { SetupDemoSnippetsUseCase(get()) }
    // Language
    factory { GetLanguagesUseCase(get(), get(), get()) }
    // Share
    factory { ShareSnippetUseCase(get()) }
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
    factory { EditInteractor(get(), get(), get(), get()) }
}