package pl.tkadziolka.snipmeandroid.domain.user

import io.reactivex.Single
import pl.tkadziolka.snipmeandroid.domain.auth.AuthorizationUseCase
import pl.tkadziolka.snipmeandroid.domain.network.CheckNetworkAvailableUseCase
import pl.tkadziolka.snipmeandroid.domain.repository.share.ShareRepository
import pl.tkadziolka.snipmeandroid.domain.repository.user.UserRepository
import pl.tkadziolka.snipmeandroid.domain.share.ShareUser
import pl.tkadziolka.snipmeandroid.util.extension.filterItems

class GetShareUsersUseCase(
    private val auth: AuthorizationUseCase,
    private val networkAvailable: CheckNetworkAvailableUseCase,
    private val shareRepository: ShareRepository,
    private val userRepository: UserRepository
) {

    operator fun invoke(loginPhrase: String, snippetUuid: String) =
        auth()
            .andThen(networkAvailable())
            .andThen(shareRepository.shareUsers(snippetUuid))
            .flatMap { users -> filterCurrentUser(users) }
            .filterItems { it.user.login.startsWith(loginPhrase, ignoreCase = true) }

    private fun filterCurrentUser(users: List<ShareUser>): Single<List<ShareUser>> =
        userRepository.user()
            .map { user -> users.filterNot { it.user.id == user.id } }
}