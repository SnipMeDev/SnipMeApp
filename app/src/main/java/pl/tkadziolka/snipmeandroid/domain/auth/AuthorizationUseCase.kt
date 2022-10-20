package pl.tkadziolka.snipmeandroid.domain.auth

import io.reactivex.Completable
import pl.tkadziolka.snipmeandroid.domain.error.exception.SessionExpiredException
import pl.tkadziolka.snipmeandroid.domain.repository.auth.AuthRepository

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