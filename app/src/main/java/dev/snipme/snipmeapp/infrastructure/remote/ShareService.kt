package dev.snipme.snipmeapp.infrastructure.remote

import io.reactivex.Completable
import io.reactivex.Single
import dev.snipme.snipmeapp.infrastructure.model.request.ShareSnippetRequest
import dev.snipme.snipmeapp.infrastructure.model.response.SharePersonResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ShareService {

    @POST("share/")
    fun share(@Body request: ShareSnippetRequest): Completable

    @GET("person-autocomplete/")
    fun shareUsers(@Query("snippet_id") snippetUuid: String): Single<List<SharePersonResponse>>
}