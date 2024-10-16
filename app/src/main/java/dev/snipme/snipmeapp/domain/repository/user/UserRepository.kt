package dev.snipme.snipmeapp.domain.repository.user

import io.reactivex.Single
import dev.snipme.snipmeapp.domain.user.User

interface UserRepository {
    fun user(): Single<User>
}