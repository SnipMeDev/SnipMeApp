package dev.snipme.snipmeapp.infrastructure.model.request

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ShareSnippetRequest(
    val access_rights: String? = null, // In future use SnippetAccess
    val snippet: String,
    val allowed_user: Int
)