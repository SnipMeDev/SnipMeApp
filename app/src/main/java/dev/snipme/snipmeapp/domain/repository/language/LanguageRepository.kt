package dev.snipme.snipmeapp.domain.repository.language

import io.reactivex.Single
import dev.snipme.snipmeapp.domain.language.SnippetLanguage

interface LanguageRepository {

    fun languages(): Single<List<SnippetLanguage>>
}