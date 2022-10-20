package pl.tkadziolka.snipmeandroid.domain.snippet

import pl.tkadziolka.snipmeandroid.domain.repository.snippet.SnippetRepository
import pl.tkadziolka.snipmeandroid.domain.snippets.Snippet

class ResetUpdatedSnippetPageUseCase(private val repository: SnippetRepository) {
    operator fun invoke() {
        repository.updateListener.onNext(Snippet.EMPTY)
    }
}