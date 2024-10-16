package dev.snipme.snipmeapp.infrastructure.model.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SnippetResponse(
    val id: String,
    val title: String?,
    val code: String?,
    val created_at: String?,
    val modified_at: String?,
    val visibility: String?,
    val owner: OwnerResponse?,
    val is_owner: Boolean?,
    val language: String?,
    val number_of_likes: Int?,
    val number_of_dislikes: Int?,
    val user_reaction: String?
)

@JsonClass(generateAdapter = true)
data class OwnerResponse(val id: Int, val username: String)