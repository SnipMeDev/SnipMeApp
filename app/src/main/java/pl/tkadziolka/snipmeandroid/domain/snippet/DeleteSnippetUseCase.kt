package pl.tkadziolka.snipmeandroid.domain.snippet

import io.reactivex.Completable
import pl.tkadziolka.snipmeandroid.domain.repository.snippet.SnippetRepository
import pl.tkadziolka.snipmeandroid.domain.snippets.Snippet

class DeleteSnippetUseCase(private val repository: SnippetRepository) {

    operator fun invoke(uuid: String): Completable =
        repository
            .delete(uuid)
//            .doOnComplete { repository.updateListener.onNext(Snippet.EMPTY.copy(uuid)) }
}