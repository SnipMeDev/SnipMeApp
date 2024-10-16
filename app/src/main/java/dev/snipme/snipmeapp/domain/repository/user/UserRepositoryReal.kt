package dev.snipme.snipmeapp.domain.repository.user

import io.reactivex.Single
import dev.snipme.snipmeapp.domain.error.ErrorHandler
import dev.snipme.snipmeapp.domain.user.User
import dev.snipme.snipmeapp.domain.user.toUser
import dev.snipme.snipmeapp.infrastructure.remote.UserService
import dev.snipme.snipmeapp.util.extension.mapError

class UserRepositoryReal(
    private val errorHandler: ErrorHandler,
    private val service: UserService
) : UserRepository {

    override fun user(): Single<User> = service.user()
        .mapError { errorHandler.handle(it) }
        .map { it.toUser() }
}