package dev.snipme.snipmeapp.infrastructure.model

import com.squareup.moshi.JsonClass
import dev.snipme.snipmeapp.infrastructure.model.response.SnippetResponse

@JsonClass(generateAdapter = true)
data class SnippetPageResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<SnippetResponse>
)