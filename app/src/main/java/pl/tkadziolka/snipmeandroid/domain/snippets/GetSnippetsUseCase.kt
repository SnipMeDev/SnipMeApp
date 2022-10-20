package pl.tkadziolka.snipmeandroid.domain.snippets

import io.reactivex.Single
import pl.tkadziolka.snipmeandroid.domain.auth.AuthorizationUseCase
import pl.tkadziolka.snipmeandroid.domain.network.CheckNetworkAvailableUseCase
import pl.tkadziolka.snipmeandroid.domain.repository.snippet.SnippetRepository

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