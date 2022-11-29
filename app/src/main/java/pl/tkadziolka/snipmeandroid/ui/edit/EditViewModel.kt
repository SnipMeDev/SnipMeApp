package pl.tkadziolka.snipmeandroid.ui.edit

import android.text.SpannableString
import androidx.navigation.NavController
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import pl.tkadziolka.snipmeandroid.domain.error.exception.*
import pl.tkadziolka.snipmeandroid.domain.message.ErrorMessages
import pl.tkadziolka.snipmeandroid.domain.message.ValidationMessages
import pl.tkadziolka.snipmeandroid.domain.reaction.UserReaction
import pl.tkadziolka.snipmeandroid.domain.snippet.EditInteractor
import pl.tkadziolka.snipmeandroid.domain.snippets.*
import pl.tkadziolka.snipmeandroid.domain.validation.FieldValidator
import pl.tkadziolka.snipmeandroid.domain.validation.messageOrNull
import pl.tkadziolka.snipmeandroid.ui.error.ErrorParsable
import pl.tkadziolka.snipmeandroid.ui.session.SessionViewModel
import pl.tkadziolka.snipmeandroid.ui.viewmodel.SingleLiveEvent
import pl.tkadziolka.snipmeandroid.ui.viewmodel.StateViewModel
import pl.tkadziolka.snipmeandroid.util.extension.inProgress
import timber.log.Timber
import java.util.*

private val NO_ERROR: String? = null

class EditViewModel(
    private val messages: ErrorMessages,
    private val errorMessages: ErrorMessages,
    private val validationMessages: ValidationMessages,
    private val navigator: EditNavigator,
    private val interactor: EditInteractor,
    private val session: SessionViewModel
) : StateViewModel<EditViewState>(), ErrorParsable {
    private val mutableEvent = SingleLiveEvent<EditEvent>()
    val event = mutableEvent

    private var createDisposable: Disposable? = null

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

    fun load(uuid: String?) {
        // Don't load the same state again when fragment is recreated
        if (state.value != null) return

        setState(Loading)

        interactor.languages()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { languages -> fetchSnippetOrComplete(languages, uuid) },
                onError = { error ->
                    Timber.e("Couldn't load languages, ERROR = $error")
                    parseError(error)
                }
            ).also { disposables += it }
    }

    fun goToDetail(nav: NavController, snippetId: String) {
        navigator.goToDetail(nav, snippetId)
    }

    fun goToLogin(nav: NavController) {
        navigator.goToLogin(nav)
    }

    fun goToError(nav: NavController, message: String?) {
        navigator.goToError(nav, message)
    }

    fun update(title: String, code: String, language: String) {
        getLoaded()?.let {
            val snip = it.snippet ?: return
            if (title != snip.title || code != snip.code.raw || language != snip.language.raw)
                setState(it.copy(snippet = getTempSnippet(title, code, language)))
        }
    }

    fun create() {
        if (state.value is Loading || createDisposable.inProgress()) return

        getSnippetOnValid()?.let { snip ->
            setState(Loading)
            createDisposable = interactor.create(snip.title, snip.code.raw, snip.language.raw)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = { snippet -> setState(Completed(snippet)) },
                    onError = { error ->
                        Timber.e("Couldn't create a new snippet, ERROR = $error")
                        mutableEvent.value = Alert(errorMessages.parse(error))
                    }
                ).also { disposables += it }
        }
    }

    fun edit(uuid: String) {
        if (state.value is Loading || createDisposable.inProgress()) return

        getSnippetOnValid()?.let { snip ->
            setState(Loading)
            createDisposable = interactor.update(
                uuid,
                snip.title,
                snip.code.raw,
                snip.language.raw,
                snip.visibility
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = { snippet -> setState(Completed(snippet)) },
                    onError = { error ->
                        Timber.e("Couldn't update the snippet, ERROR = $error")
                        mutableEvent.value = Alert(errorMessages.parse(error))
                    }
                ).also { disposables += it }
        }
    }

    fun pasteFromClipboard() {
        getLoaded()?.let {
            interactor.getFromClipboard()?.let { codeFromClipboard ->
                mutableEvent.value = PastedFromClipboard(codeFromClipboard)
            }
        }
    }

    private fun fetchSnippetOrComplete(languages: List<String>, uuid: String?) {
        if (uuid == null) {
            setState(Loaded(languages, getTempSnippet("", "", ""), NO_ERROR))
            return
        }

        interactor.snippet(uuid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { snippet -> setState(Loaded(languages, snippet, NO_ERROR)) },
                onError = { error ->
                    Timber.e("Couldn't update the snippet, ERROR = $error")
                    parseError(error)
                }
            ).also { disposables += it }
    }

    private fun getSnippetOnValid(): Snippet? {
        getLoaded()?.let {
            var escape = false
            val snip = it.snippet ?: return null

            validateFields(snip.title, snip.code.raw, snip.language.raw) { errorMessage ->
                setState(it.copy(error = errorMessage))
                escape = true
            }

            if (escape) return null

            if (it.languages.contains(snip.language.raw).not()) {
                setState(it.copy(error = messages.unknownLanguage))
                return null
            }

            return snip
        }

        return null
    }

    private fun validateFields(
        title: String,
        code: String,
        language: String,
        onError: (String) -> Unit
    ) {
        FieldValidator(validationMessages).validate(title).messageOrNull()?.let {
            onError(validationMessages.phrasesBlank)
            return
        }

        FieldValidator(validationMessages).validate(code).messageOrNull()?.let {
            onError(validationMessages.phrasesBlank)
            return
        }

        FieldValidator(validationMessages).validate(language).messageOrNull()?.let {
            onError(validationMessages.phrasesBlank)
            return
        }
    }

    private fun getLoaded(): Loaded? =
        if (state.value is Loaded) {
            state.value as Loaded
        } else {
            null
        }

    private fun getTempSnippet(title: String, code: String, language: String) = Snippet(
        uuid = "",
        title = title,
        code = SnippetCode(raw = code, highlighted = SpannableString("")),
        language = SnippetLanguage(raw = language, type = SnippetLanguageType.UNKNOWN),
        visibility = SnippetVisibility.PUBLIC,
        isOwner = false,
        owner = Owner(0, ""),
        modifiedAt = Date(),
        numberOfLikes = 0,
        numberOfDislikes = 0,
        userReaction = UserReaction.NONE
    )
}

sealed class EditViewState
object Loading : EditViewState()
data class Loaded(
    val languages: List<String>,
    val snippet: Snippet?,
    val error: String?
) : EditViewState()

data class Completed(val snippet: Snippet) : EditViewState()
data class Error(val error: String?) : EditViewState()

sealed class EditEvent
object Logout : EditEvent()
data class PastedFromClipboard(val code: String) : EditEvent()
data class Alert(val message: String) : EditEvent()