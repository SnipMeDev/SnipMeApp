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
import dev.snipme.snipmeapp.domain.message.ErrorMessages
import dev.snipme.snipmeapp.domain.reaction.GetTargetUserReactionUseCase
import dev.snipme.snipmeapp.domain.reaction.SetUserReactionUseCase
import dev.snipme.snipmeapp.domain.reaction.UserReaction
import dev.snipme.snipmeapp.domain.share.ShareSnippetCodeUseCase
import dev.snipme.snipmeapp.domain.snippet.DeleteSnippetUseCase
import dev.snipme.snipmeapp.domain.snippet.GetSingleSnippetUseCase
import dev.snipme.snipmeapp.domain.snippet.SaveSnippetUseCase
import dev.snipme.snipmeapp.domain.snippets.Snippet
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber

class DetailsModel(
    private val errorMessages: ErrorMessages,
    private val getSnippet: GetSingleSnippetUseCase,
    private val clipboard: AddToClipboardUseCase,
    private val getTargetReaction: GetTargetUserReactionUseCase,
    private val setUserReaction: SetUserReactionUseCase,
    private val saveSnippet: SaveSnippetUseCase,
    private val shareSnippet: ShareSnippetCodeUseCase,
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

    fun like() {
        changeReaction(UserReaction.LIKE)
    }

    fun dislike() {
        changeReaction(UserReaction.DISLIKE)
    }

    fun copyToClipboard() {
        getSnippet()?.let {
            clipboard(it.title, it.code.raw)
        }
    }

    fun save() {
        getSnippet()?.let {
            setState(Loading)
            saveSnippet(it)
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                    onSuccess = { saved ->
                        setState(Loaded(it))
                        mutableEvent.value = Saved(saved.uuid)
                    },
                    onError = { error ->
                        Timber.e("Couldn't save snippet, error = $error")
                        parseError(error)
                    }
                ).also { disposables += it }
        }
    }

    fun share() {
        getSnippet()?.let {
            shareSnippet(it)
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

    private fun changeReaction(newReaction: UserReaction) {
        // Immediately show change to user
        val previousState = getLoaded() ?: return
        val targetReaction = getTargetReaction(previousState.snippet, newReaction)
        setState(previousState.run { copy(snippet = snippet.copy(userReaction = targetReaction)) })

        setUserReaction(previousState.snippet, newReaction)
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = { snippet -> mutableState.value = Loaded(snippet) },
                onError = {
                    // Revert changes
                    Timber.e("Couldn't change user reaction, error = $it")
                    mutableEvent.value = Alert(errorMessages.generic)
                    setState(previousState)
                }
            ).also { disposables += it }
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
data class Saved(val snippetId: String) : DetailsEvent()
data object Logout : DetailsEvent()