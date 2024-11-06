package dev.snipme.snipmeapp.infrastructure.model.request

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginUserRequest(val username: String, val password: String)