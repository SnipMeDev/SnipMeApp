package dev.snipme.snipmeapp.domain.repository.user

import dev.snipme.snipmeapp.domain.error.ErrorHandler
import dev.snipme.snipmeapp.domain.user.User
import dev.snipme.snipmeapp.domain.user.toUser
import dev.snipme.snipmeapp.infrastructure.local.UserDao
import dev.snipme.snipmeapp.util.extension.mapError
import io.reactivex.Single

class UserRepositoryReal(
    private val errorHandler: ErrorHandler,
    private val service: UserDao
) : UserRepository {

    override fun user(id: Int): Single<User> = service.user(id)
        .mapError { errorHandler.handle(it) }
        .map { it.toUser() }
}