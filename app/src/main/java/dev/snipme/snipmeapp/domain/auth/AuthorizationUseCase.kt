package dev.snipme.snipmeapp.domain.auth

import io.reactivex.Completable
import dev.snipme.snipmeapp.domain.error.exception.SessionExpiredException
import dev.snipme.snipmeapp.domain.repository.auth.AuthRepository

// In future this should check if token is correct and up to date (refresh)
class AuthorizationUseCase(
    private val repository: AuthRepository
) {
    operator fun invoke() = repository.getToken()
        .defaultIfEmpty("")
        .flatMapCompletable { token ->
            if (token.isEmpty()) {
                Completable.error(SessionExpiredException())
            } else {
                Completable.complete()
            }
        }
}