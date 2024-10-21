package dev.snipme.snipmeapp.domain.repository.auth

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import dev.snipme.snipmeapp.infrastructure.model.response.RegisterUserResponse

interface AuthRepository {

    fun identify(login: String): Single<Boolean>

    fun login(login: String, password: String): Single<String>

    fun register(login: String, password: String, email: String): Completable

    fun saveToken(token: String): Completable

    fun getToken(): Maybe<String>

    fun clearToken(): Completable
}