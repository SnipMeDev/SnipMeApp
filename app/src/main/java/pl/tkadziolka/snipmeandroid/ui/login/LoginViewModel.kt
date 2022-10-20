package pl.tkadziolka.snipmeandroid.ui.login

import androidx.navigation.NavController
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import pl.tkadziolka.snipmeandroid.BuildConfig
import pl.tkadziolka.snipmeandroid.domain.auth.LoginInteractor
import pl.tkadziolka.snipmeandroid.domain.error.exception.*
import pl.tkadziolka.snipmeandroid.domain.message.ErrorMessages
import pl.tkadziolka.snipmeandroid.domain.message.ValidationMessages
import pl.tkadziolka.snipmeandroid.domain.validation.FieldValidator
import pl.tkadziolka.snipmeandroid.domain.validation.SignInValidator
import pl.tkadziolka.snipmeandroid.domain.validation.SignUpValidator
import pl.tkadziolka.snipmeandroid.domain.validation.messageOrNull
import pl.tkadziolka.snipmeandroid.ui.error.ErrorParsable
import pl.tkadziolka.snipmeandroid.ui.viewmodel.SingleLiveEvent
import pl.tkadziolka.snipmeandroid.ui.viewmodel.StateViewModel
import pl.tkadziolka.snipmeandroid.util.extension.errorMessage
import pl.tkadziolka.snipmeandroid.util.extension.inProgress
import retrofit2.HttpException
import timber.log.Timber

private const val DEBUG_PREFIX = "DEBUG: "

class LoginViewModel(
    private val errorMessages: ErrorMessages,
    private val messages: ValidationMessages,
    private val interactor: LoginInteractor,
    private val navigator: LoginNavigator
) : StateViewModel<LoginViewState>(), ErrorParsable {
    private var identifyDisposable: Disposable? = null
    private var loginDisposable: Disposable? = null
    private var registerDisposable: Disposable? = null

    private val mutableEvent = SingleLiveEvent<LoginEvent>()
    val event = mutableEvent

    override fun parseError(throwable: Throwable) {
        when (throwable) {
            is ConnectionException -> setEvent(Error(errorMessages.parse(throwable)))
            is ContentNotFoundException -> setEvent(Error(errorMessages.parse(throwable)))
            is ForbiddenActionException -> setEvent(Dialog(errorMessages.alreadyRegistered))
            is NetworkNotAvailableException -> setEvent(Error(errorMessages.parse(throwable)))
            is NotAuthorizedException -> setEvent(Alert(errorMessages.parse(throwable)))
            is RemoteException -> setEvent(Error(errorMessages.parse(throwable)))
            is SessionExpiredException -> setEvent(Alert(errorMessages.parse(throwable)))
            else -> setEvent(Error(errorMessages.parse(throwable)))
        }
    }

    fun startup(login: String) {
        // Don't reset state on configuration changed
        if (state.value != null) return

        setState(Startup(login, null))
    }

    fun goToLogin(login: String) {
        setState(Login(login, "", null))
    }

    fun goToMain(nav: NavController) {
        navigator.goToMain(nav)
    }

    fun goToError(nav: NavController, message: String?) {
        navigator.goToError(nav, message)
    }

    fun revokeLogin(login: String) {
        setState(Startup(login, null))
    }

    fun validateLogin(login: String) {
        if (identifyDisposable.inProgress()) return
        setState(Loading)

        FieldValidator(messages).validate(login).messageOrNull()?.let { message ->
            setState(Startup(login, message))
            return
        }

        identifyDisposable = interactor.identify(login)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { identified -> publishIdentified(login, identified) },
                onError = {
                    val errorMessage = getErrorMessage(it)
                    Timber.d("Couldn't identify user = $login, error = $it")
                    setState(Startup(login, errorMessage))
                    parseError(it)
                }
            ).also { disposables += it }
    }

    fun login(login: String, password: String) {
        if (loginDisposable.inProgress()) return
        setState(Loading)

        SignInValidator(messages).validate(login, password).messageOrNull()?.let { message ->
            setState(Login(login, password, message))
            return
        }

        loginDisposable = interactor.login(login, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = { setState(Completed(login)) },
                onError = {
                    val errorMessage = getErrorMessage(it)
                    Timber.d("Couldn't login user = $login, error = $it")
                    setState(Login(login, password, errorMessage))
                    parseError(it)
                }
            ).also { disposables += it }
    }

    fun register(login: String, password: String, repeatedPassword: String, email: String) {
        if (registerDisposable.inProgress()) return
        setState(Loading)

        SignUpValidator(messages).validate(login, password, repeatedPassword, email).messageOrNull()
            ?.let { message ->
                setState(Register(login, password, repeatedPassword, email, message))
                return
            }

        registerDisposable = interactor.register(login, password, email)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = { setState(Completed(login)) },
                onError = {
                    val errorMessage = getErrorMessage(it)
                    Timber.d("Couldn't register user = $login, error = $it")
                    setState(Register(login, password, repeatedPassword, email, errorMessage))
                    parseError(it)
                }
            ).also { disposables += it }
    }

    fun getLogin(): String? = when (state.value) {
        is Startup -> (state.value as Startup).login
        is Login -> (state.value as Login).login
        is Register -> (state.value as Register).login
        is UserFound -> (state.value as UserFound).login
        is Completed -> (state.value as Completed).login
        else -> null
    }

    private fun publishIdentified(login: String, identified: Boolean) {
        if (identified) {
            setState(UserFound(login))
        } else {
            setState(Register(login, "", "", "", null))
        }
    }

    private fun setEvent(event: LoginEvent) {
        mutableEvent.value = event
    }

    private fun getErrorMessage(throwable: Throwable): String? {
        // When detailed error message comes from API
        val snipException = throwable as? SnipException
        val httpException = snipException?.cause as? HttpException
        httpException?.errorMessage?.let { detailedMessage ->
            return detailedMessage
        }

        // Otherwise show only in debug mode
        if (BuildConfig.DEBUG.not()) {
            return null
        }

        return when (throwable) {
            is SnipException -> DEBUG_PREFIX + throwable.cause?.message
            else -> DEBUG_PREFIX + throwable.message
        }
    }
}

sealed class LoginViewState
object Loading : LoginViewState()

data class Startup(
    val login: String,
    val error: String?
) : LoginViewState()

data class Login(
    val login: String,
    val password: String,
    val error: String?
) : LoginViewState()

data class Register(
    val login: String,
    val password: String,
    val repeatedPassword: String,
    val email: String,
    val error: String?
) : LoginViewState()

data class UserFound(
    val login: String
) : LoginViewState()

data class Completed(
    val login: String
) : LoginViewState()

sealed class LoginEvent
data class Error(val message: String?) : LoginEvent()
data class Alert(val message: String) : LoginEvent()
data class Dialog(val message: String) : LoginEvent()