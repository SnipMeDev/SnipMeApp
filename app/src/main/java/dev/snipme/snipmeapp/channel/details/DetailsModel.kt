package dev.snipme.snipmeapp.channel.details

import dev.snipme.snipmeapp.channel.error.ErrorParsable
import dev.snipme.snipmeapp.channel.session.SessionModel
import dev.snipme.snipmeapp.domain.clipboard.AddToClipboardUseCase
import dev.snipme.snipmeapp.domain.error.exception.ConnectionException
import dev.snipme.snipmeapp.domain.error.exception.ContentNotFoundException
import dev.snipme.snipmeapp.domain.error.exception.ForbiddenActionException
import dev.snipme.snipmeapp.domain.error.exception.NetworkNotAvailableException
import dev.snipme.snipmeapp.domain.error.exception.NotAuthorizedException
import dev.snipme.snipmeapp.domain.error.exception.RemoteException
import dev.snipme.snipmeapp.domain.error.exception.SessionExpiredException
import dev.snipme.snipmeapp.domain.favorite.SetFavoriteSnippet
import dev.snipme.snipmeapp.domain.message.ErrorMessages
import dev.snipme.snipmeapp.domain.share.ShareSnippetUseCase
import dev.snipme.snipmeapp.domain.snippet.DeleteSnippetUseCase
import dev.snipme.snipmeapp.domain.snippet.GetSingleSnippetUseCase
import dev.snipme.snipmeapp.domain.snippet.SaveSnippetUseCase
import dev.snipme.snipmeapp.domain.snippets.Snippet
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber

class DetailsModel(
    private val errorMessages: ErrorMessages,
    private val getSnippet: GetSingleSnippetUseCase,
    private val clipboard: AddToClipboardUseCase,
    private val setFavorite: SetFavoriteSnippet,
    private val saveSnippet: SaveSnippetUseCase,
    private val shareSnippet: ShareSnippetUseCase,
    private val deleteSnippet: DeleteSnippetUseCase,
    private val session: SessionModel
) : ErrorParsable {
    private val disposables = CompositeDisposable()

    private val mutableState = MutableStateFlow<DetailsViewState>(Loading)
    val state = mutableState

    private val mutableEvent = MutableStateFlow<DetailsEvent>(Idle)
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

    fun load(uuid: String) {
        setState(Loading)
        getSnippet(uuid)
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = { setState(Loaded(it)) },
                onError = {
                    Timber.e("Couldn't load snippets, error = $it")
                    parseError(it)
                }
            ).also { disposables += it }
    }

    fun toggleFavorite() {
        getSnippet()?.let {
            // Show immediate change in UI
            val snippetWithUpdate = (state.value as Loaded).snippet.copy(favorite = !it.favorite)
            mutableState.value = (state.value as Loaded).copy(snippet = snippetWithUpdate)
            // Update field value in the background
            setFavorite(it, !it.favorite)
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                    onSuccess = { setState(Loaded(it)) },
                    onError = {
                        Timber.e("Couldn't toggle favorite, error = $it")
                        parseError(it)
                    }
                ).also { disposables += it }
        }
    }

    fun copyToClipboard() {
        getSnippet()?.let {
            clipboard(it.title, it.code.raw)
        }
    }

    fun save(image: ByteArray) {
        try {
            getSnippet()?.let {
                saveSnippet(image, it)
                Timber.d("Snippet ${it.title} saved")
            }
            mutableEvent.value = Alert("Snippet saved")
        } catch (e: Exception) {
            Timber.e("Couldn't save snippet, error = $e")
            mutableEvent.value = Alert(errorMessages.generic)
        }
    }

    fun share(image: ByteArray) {
        try {
            getSnippet()?.let { shareSnippet(image, it) }
        } catch (e: Exception) {
            Timber.e("Couldn't share snippet, error = $e")
            mutableEvent.value = Alert(errorMessages.generic)
        }
    }

    fun delete() {
        getSnippet()?.let {
            setState(Loading)
            deleteSnippet(it.uuid)
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                    onComplete = { mutableEvent.value = Deleted },
                    onError = { error ->
                        Timber.e("Couldn't delete snippet, error = $error")
                        parseError(error)
                    }
                ).also { disposables += it }
        }
    }

    private fun getSnippet(): Snippet? = getLoaded()?.snippet

    private fun getLoaded() =
        if (state.value is Loaded) {
            (state.value as Loaded)
        } else {
            null
        }

    private fun setState(newState: DetailsViewState?) {
        newState?.let { mutableState.value = it }
    }
}

sealed class DetailsViewState
data object Loading : DetailsViewState()
data class Loaded(val snippet: Snippet) : DetailsViewState()
data class Error(val error: String?) : DetailsViewState()

sealed class DetailsEvent
data object Idle : DetailsEvent()
data object Deleted : DetailsEvent()
data class Alert(val message: String) : DetailsEvent()
data object Logout : DetailsEvent()