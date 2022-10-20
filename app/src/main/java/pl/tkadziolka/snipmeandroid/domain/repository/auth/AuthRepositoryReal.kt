package pl.tkadziolka.snipmeandroid.domain.repository.auth

import io.reactivex.Completable
import io.reactivex.Maybe
import pl.tkadziolka.snipmeandroid.domain.error.ErrorHandler
import pl.tkadziolka.snipmeandroid.infrastructure.local.AuthPreferences
import pl.tkadziolka.snipmeandroid.infrastructure.model.request.IdentifyUserRequest
import pl.tkadziolka.snipmeandroid.infrastructure.model.request.LoginUserRequest
import pl.tkadziolka.snipmeandroid.infrastructure.model.request.RegisterUserRequest
import pl.tkadziolka.snipmeandroid.infrastructure.remote.AuthService
import pl.tkadziolka.snipmeandroid.util.extension.mapError

class AuthRepositoryReal(
    private val errorHandler: ErrorHandler,
    private val service: AuthService,
    private val prefs: AuthPreferences
) : AuthRepository {

    override fun identify(login: String) =
        service.identify(IdentifyUserRequest(login))
            .mapError { errorHandler.handle(it) }

    override fun login(login: String, password: String) =
        service.login(LoginUserRequest(login, password))
            .mapError { errorHandler.handle(it) }
            .map { it.token }

    override fun register(login: String, password: String, email: String) =
        service.register(RegisterUserRequest(login, password, email))
            .mapError { errorHandler.handle(it) }

    override fun saveToken(token: String) = Completable.fromCallable {
        prefs.saveToken(token)
    }

    override fun getToken(): Maybe<String> {
        val token = prefs.getToken()
        return if (token.isNullOrBlank()) {
            Maybe.empty()
        } else {
            Maybe.just(token)
        }
    }

    override fun clearToken() = Completable.fromCallable {
        prefs.clearToken()
    }
}