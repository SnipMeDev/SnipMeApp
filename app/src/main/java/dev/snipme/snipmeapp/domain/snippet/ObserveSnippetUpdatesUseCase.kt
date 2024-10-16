package dev.snipme.snipmeapp.domain.snippet

import io.reactivex.Observable
import dev.snipme.snipmeapp.domain.repository.snippet.SnippetRepository
import dev.snipme.snipmeapp.domain.snippets.Snippet

class ObserveSnippetUpdatesUseCase(private val repository: SnippetRepository) {
    operator fun invoke(): Observable<Snippet> = repository.updateListener.share()
}