package dev.snipme.snipmeapp.domain.snippets

import io.reactivex.Single
import dev.snipme.snipmeapp.domain.auth.AuthorizationUseCase
import dev.snipme.snipmeapp.domain.network.CheckNetworkAvailableUseCase
import dev.snipme.snipmeapp.domain.repository.snippet.SnippetRepository

class GetSnippetsUseCase(
    private val auth: AuthorizationUseCase,
    private val repository: SnippetRepository,
    private val networkAvailable: CheckNetworkAvailableUseCase
) {
    operator fun invoke(scope: SnippetScope, page: Int): Single<List<Snippet>> =
        auth()
            .andThen(networkAvailable())
            .andThen(
                repository.snippets(scope, page)
                    .map { list -> list.sortedByDescending { it.modifiedAt.time } }
            )
}