package pl.tkadziolka.snipmeandroid.infrastructure.model.request

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterUserRequest(val username: String, val password: String, val email: String)