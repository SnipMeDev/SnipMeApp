package pl.tkadziolka.snipmeandroid.domain.repository.user

import io.reactivex.Single
import pl.tkadziolka.snipmeandroid.domain.user.User

interface UserRepository {
    fun user(): Single<User>
}