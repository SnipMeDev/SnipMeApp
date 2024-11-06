package dev.snipme.snipmeapp.domain.filter

import dev.snipme.snipmeapp.domain.snippets.Snippet

class FilterSnippetsByScopeUseCase {
    operator fun invoke(snippets: List<Snippet>, scope: String): List<Snippet> {
        if (scope == SNIPPET_FILTER_ALL) return snippets

        return snippets.filter { it.isOwner && it.visibility.name.equals(scope, ignoreCase = true) }
    }
}