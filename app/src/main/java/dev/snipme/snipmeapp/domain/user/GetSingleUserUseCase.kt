package dev.snipme.snipmeapp.domain.user

import android.annotation.SuppressLint
import dev.snipme.snipmeapp.domain.auth.AuthorizationUseCase
import dev.snipme.snipmeapp.domain.network.CheckNetworkAvailableUseCase
import dev.snipme.snipmeapp.domain.repository.auth.AuthRepository
import dev.snipme.snipmeapp.domain.repository.user.UserRepository
import io.reactivex.Single

class GetSingleUserUseCase(
    private val auth: AuthorizationUseCase,
    private val networkAvailable: CheckNetworkAvailableUseCase,
    private val repository: UserRepository,
    private val authRepository: AuthRepository,
) {
    @SuppressLint("CheckResult")
    operator fun invoke(): Single<User> {
        var token: Int = 0
        authRepository.getToken().subscribe { value -> token = value.toInt() }
        return auth()
            .andThen(networkAvailable())
            .andThen(repository.user(token))
    }
}