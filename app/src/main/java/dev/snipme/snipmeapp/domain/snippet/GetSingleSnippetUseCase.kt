package dev.snipme.snipmeapp.domain.snippet

import io.reactivex.Single
import dev.snipme.snipmeapp.domain.auth.AuthorizationUseCase
import dev.snipme.snipmeapp.domain.network.CheckNetworkAvailableUseCase
import dev.snipme.snipmeapp.domain.repository.snippet.SnippetRepository
import dev.snipme.snipmeapp.domain.snippets.Snippet
import dev.snipme.snipmeapp.domain.user.GetSingleUserUseCase

class GetSingleSnippetUseCase(
    private val auth: AuthorizationUseCase,
    private val getSingleUser: GetSingleUserUseCase,
    private val repository: SnippetRepository
) {

    operator fun invoke(uuid: String): Single<Snippet> =
        auth()
            .andThen(getSingleUser())
            .flatMap { repository.snippet(uuid, it.id) }
}