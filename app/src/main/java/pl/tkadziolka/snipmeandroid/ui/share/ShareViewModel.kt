package pl.tkadziolka.snipmeandroid.ui.share

import androidx.navigation.NavController
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import pl.tkadziolka.snipmeandroid.domain.error.exception.*
import pl.tkadziolka.snipmeandroid.domain.message.ErrorMessages
import pl.tkadziolka.snipmeandroid.domain.share.ShareInteractor
import pl.tkadziolka.snipmeandroid.domain.share.ShareUser
import pl.tkadziolka.snipmeandroid.ui.error.ErrorParsable
import pl.tkadziolka.snipmeandroid.ui.session.SessionViewModel
import pl.tkadziolka.snipmeandroid.ui.viewmodel.SingleLiveEvent
import pl.tkadziolka.snipmeandroid.ui.viewmodel.StateViewModel
import pl.tkadziolka.snipmeandroid.util.extension.inProgress
import pl.tkadziolka.snipmeandroid.util.extension.navigateSafe

class ShareViewModel(
    private val errorMessages: ErrorMessages,
    private val interactor: ShareInteractor,
    private val session: SessionViewModel
) : StateViewModel<ShareViewState>(), ErrorParsable {
    private var usersDisposable: Disposable? = null
    private var shareDisposable: Disposable? = null

    private val mutableEvent = SingleLiveEvent<ShareEvent>()
    val event = mutableEvent

    override fun parseError(throwable: Throwable) {
        when (throwable) {
            is ConnectionException -> setState(Error(errorMessages.parse(throwable)))
            is ContentNotFoundException -> setState(Error(errorMessages.parse(throwable)))
            is ForbiddenActionException -> setState(Error(errorMessages.parse(throwable)))
            is NetworkNotAvailableException -> setState(Error(errorMessages.parse(throwable)))
            is NotAuthorizedException -> session.logOut { mutableEvent.value = Logout }
            is RemoteException -> setState(Error(errorMessages.parse(throwable)))
            is SessionExpiredException -> session.logOut { mutableEvent.value = Logout }
            else -> setState(Error(errorMessages.parse(throwable)))
        }
    }

    init {
        interactor.clearCachedUsers()
    }

    fun goToLogin(nav: NavController) {
        nav.navigateSafe(ShareFragmentDirections.goToLogin())
    }

    fun search(loginPhrase: String, snippetUuid: String) {
        usersDisposable?.dispose()

        setState(Loading)

        if (loginPhrase.isBlank()) {
            setState(Loaded(null))
            return
        }

        usersDisposable = interactor.users(loginPhrase, snippetUuid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { setState(Loaded(it)) },
                onError = { parseError(it) }
            ).also { disposables += it }
    }

    fun share(snippetUuid: String, userId: Int) {
        if (state.value is Loading || shareDisposable.inProgress()) return

        getUser(userId)?.let { user ->
            setState(Loading)
            shareDisposable = interactor.share(snippetUuid, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onComplete = { setState(Shared(user)) },
                    onError = { mutableEvent.value = Alert(errorMessages.parse(it)) }
                ).also { disposables += it }
        }
    }

    private fun getUser(id: Int): ShareUser? =
        if (state.value is Loaded) {
            (state.value as Loaded).users?.find { it.user.id == id }
        } else {
            null
        }
}

sealed class ShareViewState
object Loading : ShareViewState()
data class Loaded(val users: List<ShareUser>?) : ShareViewState()
data class Shared(val shareUser: ShareUser) : ShareViewState()
data class Error(val message: String?) : ShareViewState()

sealed class ShareEvent
data class Alert(val message: String) : ShareEvent()
object Logout : ShareEvent()