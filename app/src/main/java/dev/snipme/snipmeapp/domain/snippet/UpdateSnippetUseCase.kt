package dev.snipme.snipmeapp.domain.snippet

import io.reactivex.Single
import dev.snipme.snipmeapp.domain.auth.AuthorizationUseCase
import dev.snipme.snipmeapp.domain.network.CheckNetworkAvailableUseCase
import dev.snipme.snipmeapp.domain.repository.snippet.SnippetRepository
import dev.snipme.snipmeapp.domain.snippets.SnippetVisibility

class UpdateSnippetUseCase(
    private val auth: AuthorizationUseCase,
    private val networkAvailable: CheckNetworkAvailableUseCase,
    private val repository: SnippetRepository
) {
    operator fun invoke(
        uuid: String,
        title: String,
        code: String,
        language: String,
        visibility: SnippetVisibility,
    ) = auth()
        .andThen(networkAvailable())
        .andThen(repository.update(uuid, title, code, language, visibility))
        .flatMap {
            repository.updateListener.onNext(it)
            Single.just(it)
        }
}