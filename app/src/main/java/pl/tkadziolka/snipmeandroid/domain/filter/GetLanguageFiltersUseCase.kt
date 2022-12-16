package pl.tkadziolka.snipmeandroid.domain.filter

import pl.tkadziolka.snipmeandroid.domain.snippets.Snippet

const val SNIPPET_LANGUAGE_FILTER_ALL = "All"

class GetLanguageFiltersUseCase {

    operator fun invoke(snippets: List<Snippet>): List<String> {
        return listOf(SNIPPET_LANGUAGE_FILTER_ALL) +
                snippets.groupBy { it.language.raw }
                    .map { it.key to it.value.count() }
                    .sortedBy { it.second }.reversed()
                    .map { it.first }
                    .filter { it.isNotBlank() }
                    .distinct()
    }
}