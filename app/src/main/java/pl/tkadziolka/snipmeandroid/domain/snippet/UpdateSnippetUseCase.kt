package pl.tkadziolka.snipmeandroid.domain.snippet

import io.reactivex.Single
import pl.tkadziolka.snipmeandroid.domain.auth.AuthorizationUseCase
import pl.tkadziolka.snipmeandroid.domain.network.CheckNetworkAvailableUseCase
import pl.tkadziolka.snipmeandroid.domain.repository.snippet.SnippetRepository
import pl.tkadziolka.snipmeandroid.domain.snippets.SnippetVisibility

class UpdateSnippetUseCase(
    private val auth: AuthorizationUseCase,
    private val networkAvailable: CheckNetworkAvailableUseCase,
    private val repository: SnippetRepository
) {
    operator fun invoke(
        uuid: String,
        title: String,
        code: String,
        language: String,
        visibility: SnippetVisibility,
    ) = auth()
        .andThen(networkAvailable())
        .andThen(repository.update(uuid, title, code, language, visibility))
        .flatMap {
            repository.updateListener.onNext(it)
            Single.just(it)
        }
}