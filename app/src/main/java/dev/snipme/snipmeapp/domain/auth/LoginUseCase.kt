package dev.snipme.snipmeapp.domain.auth

import io.reactivex.Completable
import dev.snipme.snipmeapp.domain.network.CheckNetworkAvailableUseCase
import dev.snipme.snipmeapp.domain.repository.auth.AuthRepository

class LoginUseCase(
    private val auth: AuthRepository,
) {

    operator fun invoke(login: String, password: String): Completable =
        auth.login(login, password)
            .flatMapCompletable { token -> auth.saveToken(token) }
}

