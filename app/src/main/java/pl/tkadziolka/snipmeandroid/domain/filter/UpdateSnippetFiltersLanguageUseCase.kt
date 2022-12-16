package pl.tkadziolka.snipmeandroid.domain.filter

import pl.tkadziolka.snipmeandroid.domain.snippets.SnippetFilters

class UpdateSnippetFiltersLanguageUseCase {

    operator fun invoke(
        filter: SnippetFilters,
        language: String,
        isSelected: Boolean
    ): SnippetFilters = when {
        language == SNIPPET_LANGUAGE_FILTER_ALL ->
            filter.copy(selectedLanguages = listOf(SNIPPET_LANGUAGE_FILTER_ALL))
        isSelected.not() -> filter.copy(selectedLanguages = filter.selectedLanguages - language)
        else ->
            filter.copy(
                selectedLanguages = (
                        filter.selectedLanguages
                                - SNIPPET_LANGUAGE_FILTER_ALL
                                + language
                        ).distinct()
            )
    }
}