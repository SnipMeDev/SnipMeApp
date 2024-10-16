package dev.snipme.snipmeapp.infrastructure.remote

import io.reactivex.Single
import dev.snipme.snipmeapp.infrastructure.model.request.IdentifyUserRequest
import dev.snipme.snipmeapp.infrastructure.model.request.LoginUserRequest
import dev.snipme.snipmeapp.infrastructure.model.request.RegisterUserRequest
import dev.snipme.snipmeapp.infrastructure.model.response.RegisterUserResponse
import dev.snipme.snipmeapp.infrastructure.model.response.TokenResponse
import retrofit2.http.*

interface AuthService {

    @POST("check-if-user-exist/")
    fun identify(@Body request: IdentifyUserRequest): Single<Boolean>

    @POST("auth-token/")
    fun login(@Body request: LoginUserRequest): Single<TokenResponse>

    @POST("register/")
    fun register(@Body request: RegisterUserRequest): Single<RegisterUserResponse>
}