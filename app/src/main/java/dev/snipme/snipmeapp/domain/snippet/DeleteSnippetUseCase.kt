package dev.snipme.snipmeapp.domain.snippet

import io.reactivex.Completable
import dev.snipme.snipmeapp.domain.repository.snippet.SnippetRepository
import dev.snipme.snipmeapp.domain.snippets.Snippet

class DeleteSnippetUseCase(private val repository: SnippetRepository) {

    operator fun invoke(uuid: String): Completable =
        repository
            .delete(uuid)
            .doOnComplete { repository.updateListener.onNext(Snippet.EMPTY.copy(uuid)) }
}