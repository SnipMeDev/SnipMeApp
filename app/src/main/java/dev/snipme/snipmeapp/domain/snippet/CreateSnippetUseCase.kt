package dev.snipme.snipmeapp.domain.snippet

import io.reactivex.Single
import dev.snipme.snipmeapp.domain.auth.AuthorizationUseCase
import dev.snipme.snipmeapp.domain.network.CheckNetworkAvailableUseCase
import dev.snipme.snipmeapp.domain.repository.snippet.SnippetRepository
import dev.snipme.snipmeapp.domain.snippets.Snippet
import dev.snipme.snipmeapp.domain.snippets.SnippetVisibility
import dev.snipme.snipmeapp.domain.user.GetSingleUserUseCase

class CreateSnippetUseCase(
    private val auth: AuthorizationUseCase,
    private val snippetRepository: SnippetRepository,
    private val getSingleUser: GetSingleUserUseCase
) {
    operator fun invoke(
        title: String,
        code: String,
        language: String,
        visibility: SnippetVisibility = SnippetVisibility.PUBLIC
    ): Single<Snippet> = auth()
        .andThen(getSingleUser())
        .flatMap { user ->
            snippetRepository.create(
                title = title,
                code = code,
                language = language,
                visibility = visibility,
                userId = user.id
            )
        }
        .doOnSuccess { snippet ->
            snippetRepository.updateListener.onNext(snippet)
        }
}