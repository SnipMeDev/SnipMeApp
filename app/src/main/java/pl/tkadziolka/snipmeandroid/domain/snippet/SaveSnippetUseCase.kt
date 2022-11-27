package pl.tkadziolka.snipmeandroid.domain.snippet

import io.reactivex.Single
import pl.tkadziolka.snipmeandroid.domain.snippets.Snippet
import pl.tkadziolka.snipmeandroid.domain.snippets.SnippetVisibility
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