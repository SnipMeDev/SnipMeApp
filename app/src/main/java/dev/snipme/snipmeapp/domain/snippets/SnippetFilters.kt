package dev.snipme.snipmeapp.domain.snippets

data class SnippetFilters(
    val languages: List<String>,
    val selectedLanguages: List<String>,
    val scopes: List<String>,
    val selectedScope: String
)
