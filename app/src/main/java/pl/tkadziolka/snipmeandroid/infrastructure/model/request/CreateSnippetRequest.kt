package pl.tkadziolka.snipmeandroid.infrastructure.model.request

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateSnippetRequest(
    val title: String,
    val code: String,
    val language: String,
    val visibility: String
)