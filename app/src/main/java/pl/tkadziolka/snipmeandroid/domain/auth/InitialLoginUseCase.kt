package pl.tkadziolka.snipmeandroid.domain.auth

import io.reactivex.Completable
import pl.tkadziolka.snipmeandroid.domain.error.exception.NotAuthorizedException
import pl.tkadziolka.snipmeandroid.domain.repository.auth.AuthRepository

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