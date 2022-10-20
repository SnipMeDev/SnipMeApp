package pl.tkadziolka.snipmeandroid.domain.repository.share

import io.reactivex.Completable
import io.reactivex.Single
import pl.tkadziolka.snipmeandroid.domain.error.ErrorHandler
import pl.tkadziolka.snipmeandroid.domain.share.ShareUser
import pl.tkadziolka.snipmeandroid.domain.share.toShareUser
import pl.tkadziolka.snipmeandroid.infrastructure.model.request.ShareSnippetRequest
import pl.tkadziolka.snipmeandroid.infrastructure.model.response.SharePersonResponse
import pl.tkadziolka.snipmeandroid.infrastructure.remote.ShareService
import pl.tkadziolka.snipmeandroid.util.extension.mapError
import pl.tkadziolka.snipmeandroid.util.extension.mapItems

class ShareRepositoryReal(
    private val errorHandler: ErrorHandler,
    private val service: ShareService
) : ShareRepository {
    private var cachedPersonList: List<SharePersonResponse>? = null

    override fun share(uuid: String, userId: Int): Completable =
        service.share(ShareSnippetRequest(snippet = uuid, allowed_user = userId))
            .mapError { errorHandler.handle(it) }

    override fun shareUsers(snippetUuid: String): Single<List<ShareUser>> =
        getPersonList(snippetUuid)
            .mapError { errorHandler.handle(it) }
            .mapItems { it.toShareUser() }

    private fun getPersonList(snippetUuid: String): Single<List<SharePersonResponse>> =
        if (cachedPersonList != null) {
            Single.just(cachedPersonList)
        } else {
            service.shareUsers(snippetUuid).map { cachedPersonList = it; it }
        }

    override fun clearCachedUsers() {
        cachedPersonList = null
    }
}