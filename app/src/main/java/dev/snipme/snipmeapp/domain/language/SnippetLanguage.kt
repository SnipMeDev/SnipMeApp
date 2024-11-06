package dev.snipme.snipmeapp.domain.language

import dev.snipme.snipmeapp.infrastructure.model.response.LanguageResponse

data class SnippetLanguage(val name: String)

fun LanguageResponse.toLanguage(): SnippetLanguage = SnippetLanguage(name)