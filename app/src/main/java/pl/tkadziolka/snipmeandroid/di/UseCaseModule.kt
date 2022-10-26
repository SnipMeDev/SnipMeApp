package pl.tkadziolka.snipmeandroid.di

import org.koin.dsl.module
import pl.tkadziolka.snipmeandroid.domain.auth.*
import pl.tkadziolka.snipmeandroid.domain.clipboard.AddToClipboardUseCase
import pl.tkadziolka.snipmeandroid.domain.clipboard.GetFromClipboardUseCase
import pl.tkadziolka.snipmeandroid.domain.language.GetLanguagesUseCase
import pl.tkadziolka.snipmeandroid.domain.network.CheckNetworkAvailableUseCase
import pl.tkadziolka.snipmeandroid.domain.reaction.GetTargetUserReactionUseCase
import pl.tkadziolka.snipmeandroid.domain.reaction.SetUserReactionUseCase
import pl.tkadziolka.snipmeandroid.domain.share.ClearCachedShareUsersUseCase
import pl.tkadziolka.snipmeandroid.domain.share.ShareInteractor
import pl.tkadziolka.snipmeandroid.domain.share.ShareSnippetUseCase
import pl.tkadziolka.snipmeandroid.domain.snippet.*
import pl.tkadziolka.snipmeandroid.domain.snippets.GetSnippetsUseCase
import pl.tkadziolka.snipmeandroid.domain.snippets.HasMoreSnippetPagesUseCase
import pl.tkadziolka.snipmeandroid.domain.user.GetShareUsersUseCase
import pl.tkadziolka.snipmeandroid.domain.user.GetSingleUserUseCase

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
    factory { ResetUpdatedSnippetPageUseCase(get()) }
    factory { GetTargetUserReactionUseCase() }
    factory { SetUserReactionUseCase(get(), get(), get()) }
    // Language
    factory { GetLanguagesUseCase(get(), get(), get()) }
    // Share
    factory { GetShareUsersUseCase(get(), get(), get(), get()) }
    factory { ClearCachedShareUsersUseCase(get()) }
    factory { ShareSnippetUseCase(get(), get(), get(), get()) }
    // Clipboard
    factory { AddToClipboardUseCase(get()) }
    factory { GetFromClipboardUseCase(get()) }
}

internal val interactorModule = module {
    factory { LoginInteractor(get(), get(), get()) }
    factory { EditInteractor(get(), get(), get(), get(), get()) }
    factory { ShareInteractor(get(), get(), get()) }
}