package dev.snipme.snipmeapp.domain.language

import dev.snipme.snipmeapp.domain.snippets.SnippetLanguageType
import dev.snipme.snipmeapp.domain.snippets.SnippetLanguageMapper

class AvailableLanguageFilter {

    operator fun invoke(language: String): Boolean {
        val type = SnippetLanguageMapper.fromString(language)
        return type != SnippetLanguageType.UNKNOWN
    }
}