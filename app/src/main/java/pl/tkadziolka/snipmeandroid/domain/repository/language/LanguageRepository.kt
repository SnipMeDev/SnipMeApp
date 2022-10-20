package pl.tkadziolka.snipmeandroid.domain.repository.language

import io.reactivex.Single
import pl.tkadziolka.snipmeandroid.domain.language.SnippetLanguage

interface LanguageRepository {

    fun languages(): Single<List<SnippetLanguage>>
}