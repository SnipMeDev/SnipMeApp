package dev.snipme.snipmeapp.infrastructure.model.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PersonResponse(
    val id: Int?,
    val username: String?,
    val email: String?,
    val photo: String?
)

@JsonClass(generateAdapter = true)
data class SharePersonResponse(
    val id: Int?,
    val username: String?,
    val email: String?,
    val photo: String?,
    val shared: Boolean?
)