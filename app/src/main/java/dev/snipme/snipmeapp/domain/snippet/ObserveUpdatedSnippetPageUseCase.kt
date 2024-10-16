package dev.snipme.snipmeapp.domain.snippet

import io.reactivex.Observable
import io.reactivex.Single
import dev.snipme.snipmeapp.domain.repository.snippet.SnippetRepository
import dev.snipme.snipmeapp.domain.snippets.Snippet
import dev.snipme.snipmeapp.domain.snippets.SnippetScope

private const val START_PAGE = 1

class ObserveUpdatedSnippetPageUseCase(private val repository: SnippetRepository) {

    operator fun invoke(scope: SnippetScope): Observable<Int> =
        repository.updateListener
            .skipWhile { it == Snippet.EMPTY }
            .flatMapSingle { updated ->
                getPageWithUpdated(scope, updated, START_PAGE)
            }

    private fun getPageWithUpdated(
        scope: SnippetScope,
        updated: Snippet,
        page: Int
    ): Single<Int> = repository.snippets(scope, page)
        .map { snippets -> snippets.contains(updated.uuid) }
        .flatMap { contains ->
            if (contains) {
                Single.just(page)
            } else {
                // Be aware of recursion here
                getPageWithUpdated(scope, updated, page + 1)
            }
        }

    private fun List<Snippet>.contains(uuid: String) = find { it.uuid == uuid } != null
}