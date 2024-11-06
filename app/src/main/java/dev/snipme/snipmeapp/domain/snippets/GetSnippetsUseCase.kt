package dev.snipme.snipmeapp.domain.snippets

import io.reactivex.Single
import dev.snipme.snipmeapp.domain.auth.AuthorizationUseCase
import dev.snipme.snipmeapp.domain.network.CheckNetworkAvailableUseCase
import dev.snipme.snipmeapp.domain.repository.snippet.SnippetRepository
import dev.snipme.snipmeapp.domain.user.GetSingleUserUseCase

class GetSnippetsUseCase(
    private val auth: AuthorizationUseCase,
    private val repository: SnippetRepository,
    private val getSingleUser: GetSingleUserUseCase
) {
    operator fun invoke(scope: SnippetScope, page: Int): Single<List<Snippet>> =
        auth()
            .andThen(getSingleUser())
            .flatMap {
                user ->
                repository.snippets(user.id)
                    .map { list -> list.sortedByDescending { it.modifiedAt.time } }
            }
}