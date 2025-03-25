package dev.snipme.snipmeapp.domain.snippets

import dev.snipme.snipmeapp.domain.filter.SNIPPET_FILTER_ALL
import dev.snipme.snipmeapp.domain.repository.snippet.SnippetRepository
import io.reactivex.Single

class GetSnippetsUseCase(
    private val repository: SnippetRepository,
) {
    operator fun invoke(scope: SnippetScope): Single<List<Snippet>> =
        repository.snippets()
            .map {
                if (scope.visibleName.equals(SNIPPET_FILTER_ALL, ignoreCase = true)) {
                    return@map it
                } else {
                    it.filter { snippet ->
                        snippet.visibility.name.lowercase() == scope.visibleName.lowercase()
                    }
                }
            }
            .map { list -> list.sortedByDescending { it.modifiedAt.time } }
}