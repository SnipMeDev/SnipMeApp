package dev.snipme.snipmeapp.domain.repository.language

import io.reactivex.Single
import dev.snipme.snipmeapp.domain.error.ErrorHandler
import dev.snipme.snipmeapp.domain.language.AvailableLanguageFilter
import dev.snipme.snipmeapp.domain.language.SnippetLanguage
import dev.snipme.snipmeapp.domain.language.toLanguage
import dev.snipme.snipmeapp.infrastructure.remote.LanguageService
import dev.snipme.snipmeapp.util.extension.filterItems
import dev.snipme.snipmeapp.util.extension.mapError
import dev.snipme.snipmeapp.util.extension.mapItems

class LanguageRepositoryReal(
    private val errorHandler: ErrorHandler,
    private val service: LanguageService,
    private val filterAvailable: AvailableLanguageFilter
): LanguageRepository {

    override fun languages(): Single<List<SnippetLanguage>> =
        service.languages()
            .mapError { errorHandler.handle(it) }
            .filterItems { filterAvailable(it.name) }
            .mapItems { it.toLanguage() }
}