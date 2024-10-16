package dev.snipme.snipmeapp.domain.repository.auth

import io.reactivex.Completable
import io.reactivex.Maybe
import dev.snipme.snipmeapp.domain.error.ErrorHandler
import dev.snipme.snipmeapp.infrastructure.local.AuthPreferences
import dev.snipme.snipmeapp.infrastructure.model.request.IdentifyUserRequest
import dev.snipme.snipmeapp.infrastructure.model.request.LoginUserRequest
import dev.snipme.snipmeapp.infrastructure.model.request.RegisterUserRequest
import dev.snipme.snipmeapp.infrastructure.remote.AuthService
import dev.snipme.snipmeapp.util.extension.mapError

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