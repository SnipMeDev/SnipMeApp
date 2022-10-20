package pl.tkadziolka.snipmeandroid.domain.repository.auth

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import pl.tkadziolka.snipmeandroid.infrastructure.model.response.RegisterUserResponse

interface AuthRepository {

    fun identify(login: String): Single<Boolean>

    fun login(login: String, password: String): Single<String>

    fun register(login: String, password: String, email: String): Single<RegisterUserResponse>

    fun saveToken(token: String): Completable

    fun getToken(): Maybe<String>

    fun clearToken(): Completable
}