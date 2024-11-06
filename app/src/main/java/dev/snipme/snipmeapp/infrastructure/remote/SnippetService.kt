package dev.snipme.snipmeapp.infrastructure.remote

import io.reactivex.Completable
import io.reactivex.Single
import dev.snipme.snipmeapp.infrastructure.model.SnippetPageResponse
import dev.snipme.snipmeapp.infrastructure.model.request.CreateSnippetRequest
import dev.snipme.snipmeapp.infrastructure.model.request.RateSnippetRequest
import dev.snipme.snipmeapp.infrastructure.model.response.SnippetResponse
import retrofit2.http.*

private const val PATH_ID = "id"

interface SnippetService {

    @GET("snippet/")
    fun snippets(
        @Query("scope") scope: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Single<SnippetPageResponse>

    @GET("snippet/{$PATH_ID}/")
    fun snippet(@Path(PATH_ID) id: String): Single<SnippetResponse>

    @POST("snippet/")
    fun create(@Body request: CreateSnippetRequest): Single<SnippetResponse>

    @PUT("snippet/{$PATH_ID}/")
    fun update(
        @Path(PATH_ID) id: String,
        @Body request: CreateSnippetRequest
    ): Single<SnippetResponse>

    @POST("snippet-rate/")
    fun rate(@Body request: RateSnippetRequest): Completable

    @DELETE("snippet/{$PATH_ID}/")
    fun delete(@Path(PATH_ID) id: String): Completable
}