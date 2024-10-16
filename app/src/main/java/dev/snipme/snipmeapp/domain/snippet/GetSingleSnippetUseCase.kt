package dev.snipme.snipmeapp.domain.snippet

import io.reactivex.Single
import dev.snipme.snipmeapp.domain.auth.AuthorizationUseCase
import dev.snipme.snipmeapp.domain.network.CheckNetworkAvailableUseCase
import dev.snipme.snipmeapp.domain.repository.snippet.SnippetRepository
import dev.snipme.snipmeapp.domain.snippets.Snippet

class GetSingleSnippetUseCase(
    private val auth: AuthorizationUseCase,
    private val networkAvailable: CheckNetworkAvailableUseCase,
    private val repository: SnippetRepository
) {

    operator fun invoke(uuid: String): Single<Snippet> =
        auth()
            .andThen(networkAvailable())
            .andThen(repository.snippet(uuid))

}