package dev.snipme.snipmeapp.domain.auth

import io.reactivex.Completable
import dev.snipme.snipmeapp.domain.error.exception.NotAuthorizedException
import dev.snipme.snipmeapp.domain.repository.auth.AuthRepository

class InitialLoginUseCase(private val auth: AuthRepository) {
    operator fun invoke() = auth.getToken()
        .isEmpty
        .flatMapCompletable { isTokenEmpty ->
            when {
                isTokenEmpty -> Completable.error(NotAuthorizedException())
                else -> Completable.complete()
            }
        }
}