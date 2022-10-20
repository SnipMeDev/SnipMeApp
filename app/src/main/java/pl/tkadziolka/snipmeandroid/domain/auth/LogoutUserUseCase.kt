package pl.tkadziolka.snipmeandroid.domain.auth

import pl.tkadziolka.snipmeandroid.domain.repository.auth.AuthRepository

class LogoutUserUseCase(private val repository: AuthRepository) {
    operator fun invoke() = repository.clearToken()
}