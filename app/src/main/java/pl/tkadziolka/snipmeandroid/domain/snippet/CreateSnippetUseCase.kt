package pl.tkadziolka.snipmeandroid.domain.snippet

import io.reactivex.Single
import pl.tkadziolka.snipmeandroid.domain.auth.AuthorizationUseCase
import pl.tkadziolka.snipmeandroid.domain.network.CheckNetworkAvailableUseCase
import pl.tkadziolka.snipmeandroid.domain.repository.snippet.SnippetRepository
import pl.tkadziolka.snipmeandroid.domain.snippets.Snippet
import pl.tkadziolka.snipmeandroid.domain.snippets.SnippetVisibility

class CreateSnippetUseCase(
    private val auth: AuthorizationUseCase,
    private val networkAvailable: CheckNetworkAvailableUseCase,
    private val snippetRepository: SnippetRepository
) {
    operator fun invoke(
        title: String,
        code: String,
        language: String,
        visibility: SnippetVisibility = SnippetVisibility.PUBLIC
    ): Single<Snippet> = auth()
        .andThen(networkAvailable())
        .andThen(snippetRepository.create(title, code, language, visibility))
        .flatMap {
            snippetRepository.updateListener.onNext(it)
            Single.just(it)
        }
}