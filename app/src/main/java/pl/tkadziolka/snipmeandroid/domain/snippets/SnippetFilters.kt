package pl.tkadziolka.snipmeandroid.domain.snippets

data class SnippetFilters(
    val languages: List<String>,
    val selectedLanguages: List<String>,
    val scope: SnippetScope
)
