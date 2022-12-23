package pl.tkadziolka.snipmeandroid.domain.snippet

import io.reactivex.Observable
import pl.tkadziolka.snipmeandroid.domain.repository.snippet.SnippetRepository
import pl.tkadziolka.snipmeandroid.domain.snippets.Snippet

class ObserveSnippetUpdatesUseCase(private val repository: SnippetRepository) {
    operator fun invoke(): Observable<Snippet> = repository.updateListener.share()
}