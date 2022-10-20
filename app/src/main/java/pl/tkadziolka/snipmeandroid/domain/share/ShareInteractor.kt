package pl.tkadziolka.snipmeandroid.domain.share

import pl.tkadziolka.snipmeandroid.domain.user.GetShareUsersUseCase

class ShareInteractor(
    private val getUsers: GetShareUsersUseCase,
    private val shareSnippet: ShareSnippetUseCase,
    private val clearCachedShareUsers: ClearCachedShareUsersUseCase
) {

    fun users(loginPhrase: String, snippetUuid: String) = getUsers(loginPhrase, snippetUuid)

    fun share(snippetUuid: String, userId: Int) = shareSnippet(snippetUuid, userId)

    fun clearCachedUsers() = clearCachedShareUsers()
}