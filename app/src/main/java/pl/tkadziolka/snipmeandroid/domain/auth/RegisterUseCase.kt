package pl.tkadziolka.snipmeandroid.domain.auth

import io.reactivex.Completable
import pl.tkadziolka.snipmeandroid.domain.network.CheckNetworkAvailableUseCase
import pl.tkadziolka.snipmeandroid.domain.repository.auth.AuthRepository

class RegisterUseCase(
    private val auth: AuthRepository,
    private val checkNetwork: CheckNetworkAvailableUseCase
) {
    operator fun invoke(login: String, password: String, email: String): Completable =
        checkNetwork().andThen(register(login, password, email))

    private fun register(login: String, password: String, email: String) =
        auth.register(login, password, email)
            .flatMap { auth.login(login, password) }
            .flatMapCompletable { token -> auth.saveToken(token) }
}