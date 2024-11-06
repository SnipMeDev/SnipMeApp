package dev.snipme.snipmeapp.domain.auth

import dev.snipme.snipmeapp.domain.network.CheckNetworkAvailableUseCase
import dev.snipme.snipmeapp.domain.repository.auth.AuthRepository

class IdentifyUserUseCase(
    private val auth: AuthRepository,
) {
    operator fun invoke(login: String) =
        auth.identify(login)
}