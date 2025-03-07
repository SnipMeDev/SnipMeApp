package dev.snipme.snipmeapp.domain.favorite

import dev.snipme.snipmeapp.domain.repository.snippet.SnippetRepository
import dev.snipme.snipmeapp.domain.snippets.Snippet
import io.reactivex.Single

class SetFavoriteSnippet(
    private val repository: SnippetRepository,
) {
    operator fun invoke(snippet: Snippet, favorite: Boolean): Single<Snippet> =
        repository.update(
            uuid = snippet.uuid,
            title = snippet.title,
            code = snippet.code.raw,
            language = snippet.language.raw,
            visibility = snippet.visibility,
            favorite = favorite,
        )
}