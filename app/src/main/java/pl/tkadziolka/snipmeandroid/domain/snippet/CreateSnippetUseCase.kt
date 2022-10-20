package pl.tkadziolka.snipmeandroid.domain.snippet

import io.reactivex.Single
import pl.tkadziolka.snipmeandroid.domain.auth.AuthorizationUseCase
import pl.tkadziolka.snipmeandroid.domain.network.CheckNetworkAvailableUseCase
import pl.tkadziolka.snipmeandroid.domain.repository.snippet.SnippetRepository
import pl.tkadziolka.snipmeandroid.domain.snippets.Snippet

class CreateSnippetUseCase(
    private val auth: AuthorizationUseCase,
    private val networkAvailable: CheckNetworkAvailableUseCase,
    private val snippetRepository: SnippetRepository
) {
    operator fun invoke(title: String, code: String, language: String): Single<Snippet> =
        auth()
            .andThen(networkAvailable())
            .andThen(snippetRepository.create(title, code, language))
            .flatMap {
                snippetRepository.updateListener.onNext(it)
                Single.just(it)
            }
}