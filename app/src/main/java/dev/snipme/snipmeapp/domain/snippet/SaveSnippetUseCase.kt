package dev.snipme.snipmeapp.domain.snippet

import io.reactivex.Single
import dev.snipme.snipmeapp.domain.snippets.Snippet
import dev.snipme.snipmeapp.domain.snippets.SnippetVisibility
import java.util.concurrent.TimeUnit

class SaveSnippetUseCase(
    private val createSnippet: CreateSnippetUseCase
) {
    operator fun invoke(snippet: Snippet): Single<Snippet> {
        if (snippet.isOwner) return Single.just(snippet)
        return createSnippet(
            snippet.title,
            snippet.code.raw,
            snippet.language.raw,
            visibility = SnippetVisibility.PRIVATE,
        )
    }
}