package pl.tkadziolka.snipmeandroid.bridge.detail

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import pl.tkadziolka.snipmeandroid.bridge.session.SessionModel
import pl.tkadziolka.snipmeandroid.domain.clipboard.AddToClipboardUseCase
import pl.tkadziolka.snipmeandroid.domain.error.exception.*
import pl.tkadziolka.snipmeandroid.domain.message.ErrorMessages
import pl.tkadziolka.snipmeandroid.domain.reaction.GetTargetUserReactionUseCase
import pl.tkadziolka.snipmeandroid.domain.reaction.SetUserReactionUseCase
import pl.tkadziolka.snipmeandroid.domain.reaction.UserReaction
import pl.tkadziolka.snipmeandroid.domain.share.ShareSnippetCodeUseCase
import pl.tkadziolka.snipmeandroid.domain.snippet.DeleteSnippetUseCase
import pl.tkadziolka.snipmeandroid.domain.snippet.GetSingleSnippetUseCase
import pl.tkadziolka.snipmeandroid.domain.snippet.SaveSnippetUseCase
import pl.tkadziolka.snipmeandroid.domain.snippets.Snippet
import pl.tkadziolka.snipmeandroid.ui.detail.*
import pl.tkadziolka.snipmeandroid.ui.error.ErrorParsable
import timber.log.Timber

class DetailModel(
    private val errorMessages: ErrorMessages,
    private val getSnippet: GetSingleSnippetUseCase,
    private val clipboard: AddToClipboardUseCase,
    private val getTargetReaction: GetTargetUserReactionUseCase,
    private val setUserReaction: SetUserReactionUseCase,
    private val saveSnippet: SaveSnippetUseCase,
    private val shareSnippet: ShareSnippetCodeUseCase,
    private val deleteSnippet: DeleteSnippetUseCase
    private val session: SessionModel
) : ErrorParsable {
    private val disposables = CompositeDisposable()

    private val mutableState = MutableStateFlow<DetailViewState>(Loading)
    val state = mutableState

    private val mutableEvent = MutableStateFlow<DetailEvent>(Idle)
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
                    onComplete = {
                        mutableEvent.value = Deleted
                    },
                    onError = { error ->
                        Timber.e("Couldn't save snippet, error = $error")
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

    private fun setState(newState: DetailViewState?) {
        newState?.let { mutableState.value = it }
    }
}