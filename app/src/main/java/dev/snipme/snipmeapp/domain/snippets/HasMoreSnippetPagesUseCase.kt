package dev.snipme.snipmeapp.domain.snippets

import io.reactivex.Single
import dev.snipme.snipmeapp.domain.auth.AuthorizationUseCase
import dev.snipme.snipmeapp.domain.network.CheckNetworkAvailableUseCase
import dev.snipme.snipmeapp.domain.repository.snippet.SNIPPET_PAGE_SIZE
import dev.snipme.snipmeapp.domain.repository.snippet.SnippetRepository

private const val NEXT_PAGE = 1

class HasMoreSnippetPagesUseCase(
    private val auth: AuthorizationUseCase,
    private val networkAvailable: CheckNetworkAvailableUseCase,
    private val repository: SnippetRepository
) {

    operator fun invoke(scope: SnippetScope, page: Int): Single<Boolean> =
        auth()
            .andThen(networkAvailable())
            .andThen(repository.count(scope).map { count -> page < pageCountFromOverall(count) })

    private fun pageCountFromOverall(count: Int): Int {
        val nextPageOffset = count % SNIPPET_PAGE_SIZE
        val fullPages = count / SNIPPET_PAGE_SIZE
        return if (nextPageOffset > 0) {
            fullPages + NEXT_PAGE
        } else {
            fullPages
        }
    }
}