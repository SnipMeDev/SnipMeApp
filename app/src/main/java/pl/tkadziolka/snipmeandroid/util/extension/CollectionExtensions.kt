package pl.tkadziolka.snipmeandroid.util.extension

import pl.tkadziolka.snipmeandroid.domain.snippets.SnippetLanguageType
import pl.tkadziolka.snipmeandroid.domain.snippets.SnippetLanguageMapper

fun CharSequence.lines(count: Int) = lines().take(count)

fun String?.toSnippetLanguage(): SnippetLanguageType = SnippetLanguageMapper.fromString(this)