package dev.snipme.snipmeapp.domain.snippets

import dev.snipme.snipmeapp.domain.auth.AuthorizationUseCase
import dev.snipme.snipmeapp.domain.repository.snippet.SnippetRepository
import dev.snipme.snipmeapp.domain.user.GetSingleUserUseCase
import io.reactivex.Single

class GetSnippetsUseCase(
    private val auth: AuthorizationUseCase,
    private val repository: SnippetRepository,
    private val getSingleUser: GetSingleUserUseCase
) {
    operator fun invoke(scope: SnippetScope, page: Int): Single<List<Snippet>> =
        auth()
            .andThen(getSingleUser())
            .flatMap { user ->
                repository.snippets()
                    .map { list -> list.sortedByDescending { it.modifiedAt.time } }
            }
}