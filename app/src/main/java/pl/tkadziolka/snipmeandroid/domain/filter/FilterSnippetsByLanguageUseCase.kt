package pl.tkadziolka.snipmeandroid.domain.filter

import pl.tkadziolka.snipmeandroid.domain.snippets.Snippet

class FilterSnippetsByLanguageUseCase {
    operator fun invoke(snippets: List<Snippet>, languages: List<String>): List<Snippet> {
        if (languages.contains(SNIPPET_LANGUAGE_FILTER_ALL)) return snippets
        return snippets.filter { languages.contains(it.language.raw) }
    }
}