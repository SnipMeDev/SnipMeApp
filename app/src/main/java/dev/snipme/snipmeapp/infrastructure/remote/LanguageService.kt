package dev.snipme.snipmeapp.infrastructure.remote

import io.reactivex.Single
import dev.snipme.snipmeapp.infrastructure.model.response.LanguageResponse
import retrofit2.http.GET

interface LanguageService {

    @GET("language/")
    fun languages(): Single<List<LanguageResponse>>

}