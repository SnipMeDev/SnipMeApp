package pl.tkadziolka.snipmeandroid.bridge.main

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import pl.tkadziolka.snipmeandroid.bridge.session.SessionModel
import pl.tkadziolka.snipmeandroid.domain.error.exception.*
import pl.tkadziolka.snipmeandroid.domain.filter.FilterSnippetsByLanguageUseCase
import pl.tkadziolka.snipmeandroid.domain.filter.GetLanguageFiltersUseCase
import pl.tkadziolka.snipmeandroid.domain.filter.SNIPPET_LANGUAGE_FILTER_ALL
import pl.tkadziolka.snipmeandroid.domain.filter.UpdateSnippetFiltersLanguageUseCase
import pl.tkadziolka.snipmeandroid.domain.message.ErrorMessages
import pl.tkadziolka.snipmeandroid.domain.snippet.ObserveUpdatedSnippetPageUseCase
import pl.tkadziolka.snipmeandroid.domain.snippet.ResetUpdatedSnippetPageUseCase
import pl.tkadziolka.snipmeandroid.domain.snippets.*
import pl.tkadziolka.snipmeandroid.domain.user.GetSingleUserUseCase
import pl.tkadziolka.snipmeandroid.domain.user.User
import pl.tkadziolka.snipmeandroid.ui.error.ErrorParsable
import pl.tkadziolka.snipmeandroid.ui.main.*
import pl.tkadziolka.snipmeandroid.util.view.SnippetFilter
import timber.log.Timber

private const val ONE_PAGE = 1

class MainModel(
    private val errorMessages: ErrorMessages,
    private val getUser: GetSingleUserUseCase,
    private val getSnippets: GetSnippetsUseCase,
    private val observeUpdatedPage: ObserveUpdatedSnippetPageUseCase,
    private val resetUpdatedPage: ResetUpdatedSnippetPageUseCase,
    private val hasMore: HasMoreSnippetPagesUseCase,
    private val getLanguageFilters: GetLanguageFiltersUseCase,
    private val filterSnippetsByLanguage: FilterSnippetsByLanguageUseCase,
    private val updateFilterLanguage: UpdateSnippetFiltersLanguageUseCase,
    private val session: SessionModel
) : ErrorParsable {
    private val disposables = CompositeDisposable()

    private val mutableEvent = MutableStateFlow<MainEvent>(Startup)
    val event = mutableEvent

    private val mutableState = MutableStateFlow<MainViewState>(Loading)
    val state = mutableState

    private var cachedSnippets = emptyList<Snippet>()
    private var shouldRefresh = false
    private var filterState = SnippetFilters(
        languages = listOf(SNIPPET_LANGUAGE_FILTER_ALL),
        selectedLanguages = listOf(SNIPPET_LANGUAGE_FILTER_ALL),
        scope = SnippetScope.ALL
    )

    override fun parseError(throwable: Throwable) {
        when (throwable) {
            is ConnectionException -> mutableState.value = Error(errorMessages.parse(throwable))
            is ContentNotFoundException -> mutableState.value =
                Error(errorMessages.parse(throwable))
            is ForbiddenActionException -> mutableState.value =
                Error(errorMessages.parse(throwable))
            is NetworkNotAvailableException -> mutableState.value =
                Error(errorMessages.parse(throwable))
            is NotAuthorizedException -> session.logOut { mutableEvent.value = Logout }
            is RemoteException -> mutableState.value = Error(errorMessages.parse(throwable))
            is SessionExpiredException -> session.logOut { mutableEvent.value = Logout }
            else -> mutableState.value = Error(errorMessages.parse(throwable))
        }
    }

    fun initState() {
        mutableState.value = Loading
        getUser()
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = { user -> loadSnippets(user) },
                onError = {
                    Timber.e("Couldn't load user, error = $it")
                    parseError(it)
                }
            ).also { disposables += it }
    }

    fun filterLanguage(language: String, isSelected: Boolean) {
        getLoadedState()?.let {
            filterState = updateFilterLanguage(filterState, language, isSelected)
            val filteredSnippets = filterSnippetsByLanguage(cachedSnippets, filterState.selectedLanguages)
            state.value = it.copy(snippets = filteredSnippets, filters = filterState)
        }
    }

    fun filterScope(filter: SnippetFilter) {
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

    private fun loadNextPage() {
        getLoadedState()?.let { state ->
            hasMore(state.filters.scope, state.pages)
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                    onSuccess = { hasMore ->
                        if (hasMore) {
                            loadSnippets(state.user, pages = state.pages + ONE_PAGE)
                        }
                    },
                    onError = {
                        Timber.e("Couldn't check next page, error = $it")
                        mutableEvent.value = Alert(errorMessages.parse(it))
                    })
                .also { disposables += it }
        }
    }

    private fun loadSnippets(
        user: User,
        pages: Int = 1,
        scope: SnippetScope = SnippetScope.ALL
    ) {
        getSnippets(scope, pages)
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = {
                    cachedSnippets = it
                    val updatedFilters = getLanguageFilters(cachedSnippets)
                    filterState = filterState.copy(languages = updatedFilters)
                    mutableState.value = Loaded(
                        user,
                        it,
                        pages,
                        filterState
                    )
                    loadNextPage()
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

    private fun filterToScope(filter: SnippetFilter) =
        when (filter) {
            SnippetFilter.ALL -> SnippetScope.ALL
            SnippetFilter.MINE -> SnippetScope.OWNED
            else -> SnippetScope.SHARED_FOR
        }

    private fun getScope(): SnippetScope {
        getLoadedState()?.let {
            return it.filters.scope
        }
        return SnippetScope.ALL
    }
}