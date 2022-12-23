package pl.tkadziolka.snipmeandroid.domain.snippet

import pl.tkadziolka.snipmeandroid.domain.repository.snippet.SnippetRepository

class DeleteSnippetUseCase(private val repository: SnippetRepository) {

    operator fun invoke(uuid: String) = repository.delete(uuid)
}