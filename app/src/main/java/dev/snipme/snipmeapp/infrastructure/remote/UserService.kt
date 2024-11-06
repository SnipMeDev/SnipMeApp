package dev.snipme.snipmeapp.infrastructure.remote

import io.reactivex.Single
import dev.snipme.snipmeapp.infrastructure.model.response.PersonResponse
import retrofit2.http.GET

interface UserService {

    @GET("person/")
    fun user(): Single<PersonResponse>
}