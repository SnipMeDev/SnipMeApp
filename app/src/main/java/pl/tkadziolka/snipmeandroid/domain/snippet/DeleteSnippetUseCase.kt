package pl.tkadziolka.snipmeandroid.domain.snippet

import pl.tkadziolka.snipmeandroid.domain.repository.snippet.SnippetRepository
import pl.tkadziolka.snipmeandroid.domain.snippets.Snippet

class DeleteSnippetUseCase(private val repository: SnippetRepository) {

    operator fun invoke(uuid: String) =
        repository
            .delete(uuid)
            .andThen { repository.updateListener.onNext(Snippet.EMPTY) }
}