package pl.tkadziolka.snipmeandroid.domain.share

import io.reactivex.Completable
import pl.tkadziolka.snipmeandroid.domain.auth.AuthorizationUseCase
import pl.tkadziolka.snipmeandroid.domain.network.CheckNetworkAvailableUseCase
import pl.tkadziolka.snipmeandroid.domain.repository.share.ShareRepository

class ShareSnippetUseCase(
    private val auth: AuthorizationUseCase,
    private val repository: ShareRepository,
    private val networkAvailable: CheckNetworkAvailableUseCase,
    private val clearCachedUsers: ClearCachedShareUsersUseCase
) {
    operator fun invoke(snippetUuid: String, userId: Int): Completable =
        auth()
            .andThen(networkAvailable())
            .andThen(repository.share(snippetUuid, userId))
            .andThen(Completable.fromCallable { clearCachedUsers() })
}