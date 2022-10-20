package pl.tkadziolka.snipmeandroid.domain.repository.share

import io.reactivex.Completable
import io.reactivex.Single
import pl.tkadziolka.snipmeandroid.domain.share.ShareUser

interface ShareRepository {

    fun share(uuid: String, userId: Int): Completable

    fun shareUsers(snippetUuid: String): Single<List<ShareUser>>

    fun clearCachedUsers()
}