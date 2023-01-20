package pl.tkadziolka.snipmeandroid.ui.detail

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import pl.tkadziolka.snipmeandroid.domain.clipboard.AddToClipboardUseCase
import pl.tkadziolka.snipmeandroid.domain.error.exception.*
import pl.tkadziolka.snipmeandroid.domain.message.ErrorMessages
import pl.tkadziolka.snipmeandroid.domain.reaction.GetTargetUserReactionUseCase
import pl.tkadziolka.snipmeandroid.domain.reaction.SetUserReactionUseCase
import pl.tkadziolka.snipmeandroid.domain.reaction.UserReaction
import pl.tkadziolka.snipmeandroid.domain.snippet.GetSingleSnippetUseCase
import pl.tkadziolka.snipmeandroid.domain.snippets.Snippet
import pl.tkadziolka.snipmeandroid.ui.error.ErrorParsable
import pl.tkadziolka.snipmeandroid.ui.session.SessionViewModel
import pl.tkadziolka.snipmeandroid.ui.viewmodel.SingleLiveEvent
import pl.tkadziolka.snipmeandroid.ui.viewmodel.StateViewModel
import timber.log.Timber

class DetailViewModel(
    private val errorMessages: ErrorMessages,
    private val getSnippet: GetSingleSnippetUseCase,
    private val clipboard: AddToClipboardUseCase,
    private val getTargetReaction: GetTargetUserReactionUseCase,
    private val setUserReaction: SetUserReactionUseCase,
    private val session: SessionViewModel
) : StateViewModel<DetailViewState>(), ErrorParsable {

    private val mutableEvent = SingleLiveEvent<DetailEvent>()
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

    private fun changeReaction(newReaction: UserReaction) {
        // Immediately show change to user
        val previousState = getLoaded() ?: return
        val targetReaction = getTargetReaction(previousState.snippet, newReaction)
        setState(previousState.run { copy(snippet = snippet.copy(userReaction = targetReaction)) })

        setUserReaction(previousState.snippet, newReaction)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
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
}

sealed class DetailViewState
object Loading : DetailViewState()
data class Loaded(val snippet: Snippet) : DetailViewState()
data class Error(val error: String?) : DetailViewState()

sealed class DetailEvent
object Idle : DetailEvent()
object Deleted : DetailEvent()
data class Alert(val message: String) : DetailEvent()
data class Saved(val snippetId: String) : DetailEvent()
object Logout : DetailEvent()