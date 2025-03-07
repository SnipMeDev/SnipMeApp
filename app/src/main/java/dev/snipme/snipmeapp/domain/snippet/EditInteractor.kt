package dev.snipme.snipmeapp.domain.snippet

import dev.snipme.snipmeapp.domain.language.GetLanguagesUseCase
import dev.snipme.snipmeapp.domain.snippets.Snippet
import dev.snipme.snipmeapp.domain.snippets.SnippetVisibility
import io.reactivex.Single

class EditInteractor(
    private val getLanguages: GetLanguagesUseCase,
    private val getSnippet: GetSingleSnippetUseCase,
    private val createSnippet: CreateSnippetUseCase,
    private val updateSnippet: UpdateSnippetUseCase,
) {
    fun languages() = getLanguages()

    fun snippet(uuid: String) = getSnippet(uuid)

    fun create(title: String, code: String, language: String): Single<Snippet> =
        createSnippet(title, code, language)

    fun update(
        uuid: String,
        title: String,
        code: String,
        language: String,
        visibility: SnippetVisibility,
        favorite: Boolean
    ): Single<Snippet> = updateSnippet(uuid, title, code, language, visibility, favorite)
}