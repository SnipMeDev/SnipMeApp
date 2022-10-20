package pl.tkadziolka.snipmeandroid.domain.repository.language

import io.reactivex.Single
import pl.tkadziolka.snipmeandroid.domain.error.ErrorHandler
import pl.tkadziolka.snipmeandroid.domain.language.AvailableLanguageFilter
import pl.tkadziolka.snipmeandroid.domain.language.SnippetLanguage
import pl.tkadziolka.snipmeandroid.domain.language.toLanguage
import pl.tkadziolka.snipmeandroid.infrastructure.remote.LanguageService
import pl.tkadziolka.snipmeandroid.util.extension.filterItems
import pl.tkadziolka.snipmeandroid.util.extension.mapError
import pl.tkadziolka.snipmeandroid.util.extension.mapItems

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