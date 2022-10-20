package pl.tkadziolka.snipmeandroid.domain.auth

import pl.tkadziolka.snipmeandroid.domain.network.CheckNetworkAvailableUseCase
import pl.tkadziolka.snipmeandroid.domain.repository.auth.AuthRepository

class IdentifyUserUseCase(
    private val auth: AuthRepository,
    private val checkNetwork: CheckNetworkAvailableUseCase
) {
    operator fun invoke(login: String) = checkNetwork()
        .andThen(auth.identify(login))
}