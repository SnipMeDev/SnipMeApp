package pl.tkadziolka.snipmeandroid.domain.filter

import pl.tkadziolka.snipmeandroid.domain.snippets.Snippet

class FilterSnippetsByScopeUseCase {
    operator fun invoke(snippets: List<Snippet>, scope: String): List<Snippet> {
        if (scope == SNIPPET_FILTER_ALL) return snippets

        return snippets.filter { it.visibility.name.equals(scope, ignoreCase = true) }
    }
}