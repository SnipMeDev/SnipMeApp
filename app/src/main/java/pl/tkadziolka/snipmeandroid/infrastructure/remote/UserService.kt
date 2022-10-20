package pl.tkadziolka.snipmeandroid.infrastructure.remote

import io.reactivex.Single
import pl.tkadziolka.snipmeandroid.infrastructure.model.response.PersonResponse
import retrofit2.http.GET

interface UserService {

    @GET("person/")
    fun user(): Single<PersonResponse>
}