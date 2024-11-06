package dev.snipme.snipmeapp.util.extension

import dev.snipme.snipmeapp.domain.snippets.SnippetLanguageType
import dev.snipme.snipmeapp.domain.snippets.SnippetLanguageMapper

fun CharSequence.lines(count: Int) = lines().take(count)

fun String?.toSnippetLanguage(): SnippetLanguageType = SnippetLanguageMapper.fromString(this)