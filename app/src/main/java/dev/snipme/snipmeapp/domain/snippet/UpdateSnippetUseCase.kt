package dev.snipme.snipmeapp.domain.snippet

import dev.snipme.snipmeapp.domain.auth.AuthorizationUseCase
import dev.snipme.snipmeapp.domain.repository.snippet.SnippetRepository
import dev.snipme.snipmeapp.domain.snippets.SnippetVisibility
import dev.snipme.snipmeapp.domain.user.GetSingleUserUseCase
import io.reactivex.Single

class UpdateSnippetUseCase(
    private val auth: AuthorizationUseCase,
    private val getSingleUser: GetSingleUserUseCase,
    private val repository: SnippetRepository
) {
    operator fun invoke(
        uuid: String,
        title: String,
        code: String,
        language: String,
        visibility: SnippetVisibility,
        favorite: Boolean
    ) = auth()
        .andThen(getSingleUser())
        .flatMap { repository.update(uuid, title, code, language, visibility, favorite) }
        .doOnSuccess() {
            repository.updateListener.onNext(it)
            Single.just(it)
        }
}