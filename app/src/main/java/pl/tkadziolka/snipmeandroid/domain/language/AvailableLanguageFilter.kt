package pl.tkadziolka.snipmeandroid.domain.language

import pl.tkadziolka.snipmeandroid.domain.snippets.SnippetLanguageType
import pl.tkadziolka.snipmeandroid.domain.snippets.SnippetLanguageMapper

class AvailableLanguageFilter {

    operator fun invoke(language: String): Boolean {
        val type = SnippetLanguageMapper.fromString(language)
        return type != SnippetLanguageType.UNKNOWN
    }
}