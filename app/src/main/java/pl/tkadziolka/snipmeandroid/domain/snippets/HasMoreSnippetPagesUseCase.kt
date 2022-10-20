package pl.tkadziolka.snipmeandroid.domain.snippets

import io.reactivex.Single
import pl.tkadziolka.snipmeandroid.domain.auth.AuthorizationUseCase
import pl.tkadziolka.snipmeandroid.domain.network.CheckNetworkAvailableUseCase
import pl.tkadziolka.snipmeandroid.domain.repository.snippet.SNIPPET_PAGE_SIZE
import pl.tkadziolka.snipmeandroid.domain.repository.snippet.SnippetRepository

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