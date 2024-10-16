package dev.snipme.snipmeapp.domain.snippet

import io.reactivex.Single
import dev.snipme.snipmeapp.domain.auth.AuthorizationUseCase
import dev.snipme.snipmeapp.domain.network.CheckNetworkAvailableUseCase
import dev.snipme.snipmeapp.domain.repository.snippet.SnippetRepository
import dev.snipme.snipmeapp.domain.snippets.Snippet
import dev.snipme.snipmeapp.domain.snippets.SnippetVisibility

class CreateSnippetUseCase(
    private val auth: AuthorizationUseCase,
    private val networkAvailable: CheckNetworkAvailableUseCase,
    private val snippetRepository: SnippetRepository
) {
    operator fun invoke(
        title: String,
        code: String,
        language: String,
        visibility: SnippetVisibility = SnippetVisibility.PUBLIC
    ): Single<Snippet> = auth()
        .andThen(networkAvailable())
        .andThen(snippetRepository.create(title, code, language, visibility))
        .flatMap {
            snippetRepository.updateListener.onNext(it)
            Single.just(it)
        }
}