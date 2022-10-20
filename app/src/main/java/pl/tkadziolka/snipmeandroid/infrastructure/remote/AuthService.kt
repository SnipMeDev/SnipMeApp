package pl.tkadziolka.snipmeandroid.infrastructure.remote

import io.reactivex.Single
import pl.tkadziolka.snipmeandroid.infrastructure.model.request.IdentifyUserRequest
import pl.tkadziolka.snipmeandroid.infrastructure.model.request.LoginUserRequest
import pl.tkadziolka.snipmeandroid.infrastructure.model.request.RegisterUserRequest
import pl.tkadziolka.snipmeandroid.infrastructure.model.response.RegisterUserResponse
import pl.tkadziolka.snipmeandroid.infrastructure.model.response.TokenResponse
import retrofit2.http.*

interface AuthService {

    @POST("check-if-user-exist/")
    fun identify(@Body request: IdentifyUserRequest): Single<Boolean>

    @POST("auth-token/")
    fun login(@Body request: LoginUserRequest): Single<TokenResponse>

    @POST("register/")
    fun register(@Body request: RegisterUserRequest): Single<RegisterUserResponse>
}