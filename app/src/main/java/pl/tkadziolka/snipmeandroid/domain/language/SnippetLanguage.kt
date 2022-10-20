package pl.tkadziolka.snipmeandroid.domain.language

import pl.tkadziolka.snipmeandroid.infrastructure.model.response.LanguageResponse

data class SnippetLanguage(val name: String)

fun LanguageResponse.toLanguage(): SnippetLanguage = SnippetLanguage(name)