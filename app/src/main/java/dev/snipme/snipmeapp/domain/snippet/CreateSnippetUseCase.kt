package dev.snipme.snipmeapp.domain.snippet

import dev.snipme.snipmeapp.domain.auth.AuthorizationUseCase
import dev.snipme.snipmeapp.domain.repository.snippet.SnippetRepository
import dev.snipme.snipmeapp.domain.snippets.Snippet
import dev.snipme.snipmeapp.domain.snippets.SnippetVisibility
import dev.snipme.snipmeapp.domain.user.GetSingleUserUseCase
import io.reactivex.Single

class CreateSnippetUseCase(
    private val auth: AuthorizationUseCase,
    private val snippetRepository: SnippetRepository,
    private val getSingleUser: GetSingleUserUseCase
) {
    operator fun invoke(
        title: String,
        code: String,
        language: String,
        visibility: SnippetVisibility = SnippetVisibility.PUBLIC,
        favorite: Boolean = false,
    ): Single<Snippet> = auth()
        .andThen(getSingleUser())
        .flatMap { user ->
            snippetRepository.create(
                title = title,
                code = code,
                language = language,
                visibility = visibility,
                userId = user.id,
                favorite = favorite
            )
        }
        .doOnSuccess { snippet ->
            snippetRepository.updateListener.onNext(snippet)
        }
}