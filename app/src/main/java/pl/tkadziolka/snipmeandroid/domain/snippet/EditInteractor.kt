package pl.tkadziolka.snipmeandroid.domain.snippet

import io.reactivex.Single
import pl.tkadziolka.snipmeandroid.domain.clipboard.GetFromClipboardUseCase
import pl.tkadziolka.snipmeandroid.domain.language.GetLanguagesUseCase
import pl.tkadziolka.snipmeandroid.domain.snippets.Snippet
import pl.tkadziolka.snipmeandroid.domain.snippets.SnippetVisibility

class EditInteractor(
    private val getLanguages: GetLanguagesUseCase,
    private val getSnippet: GetSingleSnippetUseCase,
    private val createSnippet: CreateSnippetUseCase,
    private val updateSnippet: UpdateSnippetUseCase,
    private val fromClipboard: GetFromClipboardUseCase
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
    ): Single<Snippet> = updateSnippet(uuid, title, code, language, visibility)

    fun getFromClipboard(): String? = fromClipboard()
}