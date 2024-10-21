package dev.snipme.snipmeapp.domain.repository.auth

import android.util.Log
import io.reactivex.Completable
import io.reactivex.Maybe
import dev.snipme.snipmeapp.domain.error.ErrorHandler
import dev.snipme.snipmeapp.infrastructure.local.AuthPreferences
import dev.snipme.snipmeapp.infrastructure.local.UserEntry
import dev.snipme.snipmeapp.infrastructure.local.UserDao
import dev.snipme.snipmeapp.util.extension.mapError
import io.reactivex.Single

class AuthRepositoryReal(
    private val errorHandler: ErrorHandler,
    private val service: UserDao,
    private val prefs: AuthPreferences
) : AuthRepository {

    override fun identify(login: String) =
        service.identify(login)
            .mapError { errorHandler.handle(it) }
            .map { it -> it > 0 }


    override fun login(login: String, password: String) =
        service.login(login, password)
            .mapError { errorHandler.handle(it) }
            .map { it }

    override fun register(login: String, password: String, email: String) =
        service.register(UserEntry(email, password))
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