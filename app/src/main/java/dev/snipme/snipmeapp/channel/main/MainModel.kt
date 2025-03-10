package dev.snipme.snipmeapp.channel.main

import dev.snipme.snipmeapp.channel.error.ErrorParsable
import dev.snipme.snipmeapp.channel.session.SessionModel
import dev.snipme.snipmeapp.domain.error.exception.ConnectionException
import dev.snipme.snipmeapp.domain.error.exception.ContentNotFoundException
import dev.snipme.snipmeapp.domain.error.exception.ForbiddenActionException
import dev.snipme.snipmeapp.domain.error.exception.NetworkNotAvailableException
import dev.snipme.snipmeapp.domain.error.exception.NotAuthorizedException
import dev.snipme.snipmeapp.domain.error.exception.RemoteException
import dev.snipme.snipmeapp.domain.error.exception.SessionExpiredException
import dev.snipme.snipmeapp.domain.filter.FilterSnippetsByLanguageUseCase
import dev.snipme.snipmeapp.domain.filter.FilterSnippetsByScopeUseCase
import dev.snipme.snipmeapp.domain.filter.GetLanguageFiltersUseCase
import dev.snipme.snipmeapp.domain.filter.SNIPPET_FILTER_ALL
import dev.snipme.snipmeapp.domain.filter.UpdateSnippetFiltersLanguageUseCase
import dev.snipme.snipmeapp.domain.message.ErrorMessages
import dev.snipme.snipmeapp.domain.snippet.ObserveSnippetUpdatesUseCase
import dev.snipme.snipmeapp.domain.snippets.GetSnippetsUseCase
import dev.snipme.snipmeapp.domain.snippets.HasMoreSnippetPagesUseCase
import dev.snipme.snipmeapp.domain.snippets.SetupDemoSnippetsUseCase
import dev.snipme.snipmeapp.domain.snippets.Snippet
import dev.snipme.snipmeapp.domain.snippets.SnippetFilters
import dev.snipme.snipmeapp.domain.snippets.SnippetScope
import dev.snipme.snipmeapp.domain.user.User
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber

private const val ONE_PAGE = 1

class MainModel(
    private val errorMessages: ErrorMessages,
    private val setupDemoSnippets: SetupDemoSnippetsUseCase,
    private val getSnippets: GetSnippetsUseCase,
    private val observeUpdates: ObserveSnippetUpdatesUseCase,
    private val hasMore: HasMoreSnippetPagesUseCase,
    private val getLanguageFilters: GetLanguageFiltersUseCase,
    private val filterSnippetsByLanguage: FilterSnippetsByLanguageUseCase,
    private val filterSnippetsByScope: FilterSnippetsByScopeUseCase,
    private val updateFilterLanguage: UpdateSnippetFiltersLanguageUseCase,
    private val session: SessionModel
) : ErrorParsable {
    private val disposables = CompositeDisposable()

    private val mutableEvent = MutableStateFlow<MainEvent>(Startup)
    val event = mutableEvent

    private val mutableState = MutableStateFlow<MainViewState>(Loading)
    val state = mutableState

    private var cachedSnippets = emptyList<Snippet>()
    private var scopedSnippets = emptyList<Snippet>()
    private lateinit var filterState: SnippetFilters

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

    init {
        observeUpdates()
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onNext = { initState() },
                onError = { Timber.e("Couldn't refresh snippet updates, error = $it") }
            ).also { disposables += it }
    }

    fun initState() {
        mutableState.value = Loading
        filterState = SnippetFilters(
            languages = listOf(SNIPPET_FILTER_ALL),
            selectedLanguages = listOf(SNIPPET_FILTER_ALL),
            scopes = listOf("All", "Private", "Public"),
            selectedScope = "All"
        )

        setupDemoSnippets()
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onComplete = { loadSnippets() },
                onError = {
                    Timber.e("Couldn't setup demo snippets, error = $it")
                    parseError(it)
                }
            ).also { disposables += it }
    }

    fun filterLanguage(language: String, isSelected: Boolean) {
        getLoadedState()?.let {
            filterState = updateFilterLanguage(filterState, language, isSelected)
            val filteredSnippets =
                filterSnippetsByLanguage(scopedSnippets, filterState.selectedLanguages)
            state.value = it.copy(snippets = filteredSnippets, filters = filterState)
        }
    }

    fun filterScope(scope: String) {
        getLoadedState()?.let {
            filterState = filterState.copy(selectedScope = scope)
            scopedSnippets = filterSnippetsByScope(cachedSnippets, scope)
            val updatedFilters = getLanguageFilters(scopedSnippets)
            filterState = filterState.copy(
                languages = updatedFilters,
                selectedLanguages = listOf(SNIPPET_FILTER_ALL),
            )
            state.value = it.copy(snippets = scopedSnippets, filters = filterState)
        }
    }

    fun logOut() {
        session.logOut { mutableEvent.value = Logout }
    }

    private fun loadNextPage() {
        getLoadedState()?.let { state ->
            hasMore(SnippetScope.ALL, state.pages)
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                    onSuccess = { hasMore ->
                        if (hasMore) {
                            loadSnippets(pages = state.pages + ONE_PAGE)
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
        pages: Int = 1,
        scope: SnippetScope = SnippetScope.ALL
    ) {
        getSnippets(scope, pages)
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = {
                    cachedSnippets = it
                    scopedSnippets = cachedSnippets
                    val updatedFilters = getLanguageFilters(cachedSnippets)
                    filterState = filterState.copy(languages = updatedFilters)
                    mutableState.value = Loaded(
                        User(0, "login", "email", ""), // TODO Remove
                        it,
                        pages,
                        filterState
                    )
                    loadNextPage()
                },
                onError = {
                    Timber.e("Couldn't load snippets, error = $it")
                    parseError(it)
                }
            ).also { disposables += it }
    }

    private fun getLoadedState(): Loaded? = state.value as? Loaded
}

sealed class MainViewState
data object Loading : MainViewState()
data class Loaded(
    val user: User,
    val snippets: List<Snippet>,
    val pages: Int,
    val filters: SnippetFilters
) : MainViewState()

data class Error(val message: String?) : MainViewState()

sealed class MainEvent
data object Startup : MainEvent()
data class Alert(val message: String) : MainEvent()
data object Logout : MainEvent()