package pl.tkadziolka.snipmeandroid.infrastructure.model

import com.squareup.moshi.JsonClass
import pl.tkadziolka.snipmeandroid.infrastructure.model.response.SnippetResponse

@JsonClass(generateAdapter = true)
data class SnippetPageResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<SnippetResponse>
)