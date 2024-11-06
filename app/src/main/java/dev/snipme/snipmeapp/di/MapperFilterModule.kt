package dev.snipme.snipmeapp.di

import org.koin.dsl.module
import dev.snipme.snipmeapp.domain.language.AvailableLanguageFilter
import dev.snipme.snipmeapp.domain.snippets.SnippetResponseMapper

internal val mapperFilterModule = module {
    factory { SnippetResponseMapper() }
    factory { AvailableLanguageFilter() }
}