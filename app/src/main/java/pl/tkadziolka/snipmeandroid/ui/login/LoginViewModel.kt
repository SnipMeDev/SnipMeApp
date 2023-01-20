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
) : StateViewModel<LoginState>(), ErrorParsable {
    private var loginDisposable: Disposable? = null
    private var registerDisposable: Disposable? = null

    private val mutableEvent = SingleLiveEvent<LoginEvent>()
    val event = mutableEvent

    override fun parseError(throwable: Throwable) {
        when (throwable) {
            is ConnectionException -> setEvent(Error(errorMessages.parse(throwable)))
            is ContentNotFoundException -> setEvent(Error(errorMessages.parse(throwable)))
            is ForbiddenActionException -> setEvent(Error(errorMessages.alreadyRegistered))
            is NetworkNotAvailableException -> setEvent(Error(errorMessages.parse(throwable)))
            is NotAuthorizedException -> setEvent(Error(errorMessages.parse(throwable)))
            is RemoteException -> setEvent(Error(errorMessages.parse(throwable)))
            is SessionExpiredException -> setEvent(Error(errorMessages.parse(throwable)))
            else -> setEvent(Error(errorMessages.parse(throwable)))
        }
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

sealed class LoginState
object Loading : LoginState()
object Loaded : LoginState()

data class Startup(
    val login: String,
    val error: String?
) : LoginState()

data class Login(
    val login: String,
    val password: String,
    val error: String?
) : LoginState()

data class Register(
    val login: String,
    val password: String,
    val repeatedPassword: String,
    val email: String,
    val error: String?
) : LoginState()

data class UserFound(
    val login: String
) : LoginState()

data class Completed(
    val login: String
) : LoginState()

sealed class LoginEvent
object Idle : LoginEvent()
object Logged : LoginEvent()
data class Error(val message: String?) : LoginEvent()