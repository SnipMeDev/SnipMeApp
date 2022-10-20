package pl.tkadziolka.snipmeandroid.infrastructure.remote

import io.reactivex.Single
import pl.tkadziolka.snipmeandroid.infrastructure.model.response.LanguageResponse
import retrofit2.http.GET

interface LanguageService {

    @GET("language/")
    fun languages(): Single<List<LanguageResponse>>

}