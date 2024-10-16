package dev.snipme.snipmeapp.domain.filter

import dev.snipme.snipmeapp.domain.snippets.Snippet

class FilterSnippetsByLanguageUseCase {
    operator fun invoke(snippets: List<Snippet>, languages: List<String>): List<Snippet> {
        if (languages.contains(SNIPPET_FILTER_ALL)) return snippets
        return snippets.filter { languages.contains(it.language.raw) }
    }
}