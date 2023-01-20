package pl.tkadziolka.snipmeandroid.ui.main

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import pl.tkadziolka.snipmeandroid.bridge.Bridge
import pl.tkadziolka.snipmeandroid.domain.error.exception.*
import pl.tkadziolka.snipmeandroid.domain.message.ErrorMessages
import pl.tkadziolka.snipmeandroid.domain.snippet.ObserveUpdatedSnippetPageUseCase
import pl.tkadziolka.snipmeandroid.domain.snippet.ResetUpdatedSnippetPageUseCase
import pl.tkadziolka.snipmeandroid.domain.snippets.*
import pl.tkadziolka.snipmeandroid.domain.user.GetSingleUserUseCase
import pl.tkadziolka.snipmeandroid.domain.user.User
import pl.tkadziolka.snipmeandroid.ui.error.ErrorParsable
import pl.tkadziolka.snipmeandroid.ui.session.SessionViewModel
import pl.tkadziolka.snipmeandroid.ui.viewmodel.PersistedStateViewModel
import pl.tkadziolka.snipmeandroid.ui.viewmodel.SingleLiveEvent
import timber.log.Timber

private const val ONE_PAGE = 1

class MainViewModel(
    private val errorMessages: ErrorMessages,
    private val getUser: GetSingleUserUseCase,
    private val getSnippets: GetSnippetsUseCase,
    private val observeUpdatedPage: ObserveUpdatedSnippetPageUseCase,
    private val resetUpdatedPage: ResetUpdatedSnippetPageUseCase,
    private val hasMore: HasMoreSnippetPagesUseCase,
    private val session: SessionViewModel
) : PersistedStateViewModel<MainViewState>(), ErrorParsable {
    private var shouldRefresh = false

    private val mutableEvent = SingleLiveEvent<MainEvent>()
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

    override fun initState() {
        setState(Loading)

        getUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { user -> loadSnippets(user) },
                onError = {
                    Timber.e("Couldn't load user, error = $it")
                    parseError(it)
                }
            ).also { disposables += it }
    }

    fun filter(filter: Bridge.SnippetFilter) {
        val scope = filterToScope(filter)
        getLoadedState()?.let { state ->
            loadSnippets(state.user, pages = ONE_PAGE, scope = scope)
            mutableEvent.value = ListRefreshed
        }
    }

    fun logOut() {
        session.logOut { mutableEvent.value = Logout }
    }

    fun refreshSnippetUpdates() {
        getLoadedState()?.let {
            observeUpdatedPage(getScope())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onNext = { updatedPage ->
                        shouldRefresh = true
                        loadSnippets(it.user, updatedPage, getScope())
                        resetUpdatedPage()
                    },
                    onError = { Timber.e("Couldn't refresh snippet updates, error = $it") }
                ).also { disposables += it }
        }
    }

    private fun loadSnippets(
        user: User,
        pages: Int = 1,
        scope: SnippetScope = SnippetScope.ALL
    ) {
        setState(Loading)
        getSnippets(scope, pages)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
//                    setState(Loaded(user, it, pages))
                    if (shouldRefresh) {
                        mutableEvent.value = ListRefreshed
                        shouldRefresh = false
                    }
                },
                onError = {
                    Timber.e("Couldn't load snippets, error = $it")
                    parseError(it)
                }
            ).also { disposables += it }
    }

    private fun getLoadedState(): Loaded? = state.value as? Loaded

    private fun filterToScope(filter: Bridge.SnippetFilter) =
        when (filter) {
//            Bridge.SnippetFilter.ALL -> SnippetScope.ALL
//            Bridge.SnippetFilter.MINE -> SnippetScope.OWNED
            else -> SnippetScope.SHARED_FOR
        }

    private fun getScope(): SnippetScope {
        getLoadedState()?.let {

        }
        return SnippetScope.ALL
    }
}

sealed class MainViewState
object Loading : MainViewState()
data class Loaded(
    val user: User,
    val snippets: List<Snippet>,
    val pages: Int,
    val filters: SnippetFilters
) : MainViewState()

data class Error(val message: String?) : MainViewState()

sealed class MainEvent
object Startup : MainEvent()
object ListRefreshed : MainEvent()
data class Alert(val message: String) : MainEvent()
object Logout : MainEvent()