package dev.snipme.snipmeapp.infrastructure.model.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LanguageResponse(val name: String)