package pl.tkadziolka.snipmeandroid.domain.auth

import io.reactivex.Completable
import pl.tkadziolka.snipmeandroid.domain.network.CheckNetworkAvailableUseCase
import pl.tkadziolka.snipmeandroid.domain.repository.auth.AuthRepository

class LoginUseCase(
    private val auth: AuthRepository,
    private val checkNetwork: CheckNetworkAvailableUseCase
) {

    operator fun invoke(login: String, password: String): Completable = checkNetwork()
        .andThen(
            auth.login(login, password)
                .flatMapCompletable { token -> auth.saveToken(token) }
        )
}