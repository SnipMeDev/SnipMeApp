package pl.tkadziolka.snipmeandroid.domain.repository.user

import io.reactivex.Single
import pl.tkadziolka.snipmeandroid.domain.error.ErrorHandler
import pl.tkadziolka.snipmeandroid.domain.user.User
import pl.tkadziolka.snipmeandroid.domain.user.toUser
import pl.tkadziolka.snipmeandroid.infrastructure.remote.UserService
import pl.tkadziolka.snipmeandroid.util.extension.mapError

class UserRepositoryReal(
    private val errorHandler: ErrorHandler,
    private val service: UserService
) : UserRepository {

    override fun user(): Single<User> = service.user()
        .mapError { errorHandler.handle(it) }
        .map { it.toUser() }
}