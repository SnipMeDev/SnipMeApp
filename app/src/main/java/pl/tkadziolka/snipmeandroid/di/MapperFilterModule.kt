package pl.tkadziolka.snipmeandroid.di

import org.koin.dsl.module
import pl.tkadziolka.snipmeandroid.domain.language.AvailableLanguageFilter
import pl.tkadziolka.snipmeandroid.domain.snippets.SnippetResponseMapper

internal val mapperFilterModule = module {
    factory { SnippetResponseMapper() }
    factory { AvailableLanguageFilter() }
}