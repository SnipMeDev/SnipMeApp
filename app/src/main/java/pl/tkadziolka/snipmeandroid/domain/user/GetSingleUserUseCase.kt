package pl.tkadziolka.snipmeandroid.domain.user

import pl.tkadziolka.snipmeandroid.domain.auth.AuthorizationUseCase
import pl.tkadziolka.snipmeandroid.domain.network.CheckNetworkAvailableUseCase
import pl.tkadziolka.snipmeandroid.domain.repository.user.UserRepository

class GetSingleUserUseCase(
    private val auth: AuthorizationUseCase,
    private val networkAvailable: CheckNetworkAvailableUseCase,
    private val repository: UserRepository
) {

    operator fun invoke() =
        auth()
            .andThen(networkAvailable())
            .andThen(repository.user())

}