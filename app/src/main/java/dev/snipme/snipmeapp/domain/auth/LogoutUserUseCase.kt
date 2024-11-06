package dev.snipme.snipmeapp.domain.auth

import dev.snipme.snipmeapp.domain.repository.auth.AuthRepository

class LogoutUserUseCase(private val repository: AuthRepository) {
    operator fun invoke() = repository.clearToken()
}