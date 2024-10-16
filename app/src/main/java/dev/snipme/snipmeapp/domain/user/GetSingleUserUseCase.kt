package dev.snipme.snipmeapp.domain.user

import dev.snipme.snipmeapp.domain.auth.AuthorizationUseCase
import dev.snipme.snipmeapp.domain.network.CheckNetworkAvailableUseCase
import dev.snipme.snipmeapp.domain.repository.user.UserRepository

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