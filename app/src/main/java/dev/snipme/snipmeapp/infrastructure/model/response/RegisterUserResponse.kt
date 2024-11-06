package dev.snipme.snipmeapp.infrastructure.model.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterUserResponse(
    val id: Int?,
    val username: String?,
    val password: String?,
    val email: String?,
    val first_name: String?,
    val last_name: String?,
    val date_joined: String?,
    val groups: List<Any>?,
    val is_active: Boolean?,
    val is_staff: Boolean?,
    val is_superuser: Boolean?,
    val last_login: Any?,
    val user_permissions: List<Any>?
)