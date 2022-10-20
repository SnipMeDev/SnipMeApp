package pl.tkadziolka.snipmeandroid.domain.user

import androidx.annotation.VisibleForTesting
import pl.tkadziolka.snipmeandroid.infrastructure.model.response.PersonResponse

data class User(val id: Int, val login: String, val email: String, val photo: String) {
    companion object {
        @VisibleForTesting
        fun mock() = User(0, "test login", "test email", "test photo")
    }
}

fun PersonResponse.toUser() = User(
    id = id ?: throw IllegalArgumentException("User must have an id!"),
    login = username ?: throw IllegalArgumentException("User must have a login!"),
    email = email ?: "",
    photo = photo ?: ""
)