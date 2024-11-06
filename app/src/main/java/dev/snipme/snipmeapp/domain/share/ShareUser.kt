package dev.snipme.snipmeapp.domain.share

import dev.snipme.snipmeapp.domain.user.User
import dev.snipme.snipmeapp.infrastructure.model.response.SharePersonResponse

data class ShareUser(val user: User, val isShared: Boolean)

fun SharePersonResponse.toShareUser() = ShareUser(
    isShared = shared ?: throw IllegalArgumentException("Shared param must be defined!"),
    user = User(
        id = id ?: throw IllegalArgumentException("User must have an id!"),
        login = username ?: throw IllegalArgumentException("User must have a login!"),
        email = email ?: "",
        photo = photo ?: ""
    )
)