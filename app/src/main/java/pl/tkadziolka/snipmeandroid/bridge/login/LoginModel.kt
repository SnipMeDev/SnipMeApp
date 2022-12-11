package pl.tkadziolka.snipmeandroid.bridge.login

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import pl.tkadziolka.snipmeandroid.domain.auth.LoginInteractor
import pl.tkadziolka.snipmeandroid.domain.error.exception.*
import pl.tkadziolka.snipmeandroid.domain.message.ErrorMessages
import pl.tkadziolka.snipmeandroid.ui.error.ErrorParsable
import pl.tkadziolka.snipmeandroid.ui.login.*
import pl.tkadziolka.snipmeandroid.util.extension.inProgress
import timber.log.Timber

class LoginModel(
    private val errorMessages: ErrorMessages,
    private val interactor: LoginInteractor,
): ErrorParsable {
    private val disposables = CompositeDisposable()
    private var identifyDisposable: Disposable? = null
    private var loginDisposable: Disposable? = null
    private var registerDisposable: Disposable? = null

    private val mutableEvent = MutableStateFlow<LoginEvent>(Idle)
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

    fun loginOrRegister(email: String, password: String) {
        if (identifyDisposable.inProgress()) return

        identifyDisposable = interactor.identify(email)
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = { identified -> publishIdentified(email, password, identified) },
                onError = {
                    Timber.d("Couldn't identify user = $email, error = $it")
                    parseError(it)
                }
            ).also { disposables += it }
    }

    fun login(email: String, password: String) {
        if (loginDisposable.inProgress()) return

        loginDisposable = interactor.login(email, password)
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onComplete = { setEvent(Logged) },
                onError = {
                    Timber.d("Couldn't login user = $email, error = $it")
                    parseError(it)
                }
            ).also { disposables += it }
    }

    fun register(email: String, password: String) {
        if (registerDisposable.inProgress()) return

        registerDisposable = interactor.register(email, password, email)
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onComplete = { setEvent(Logged) },
                onError = {
                    Timber.d("Couldn't register user = $email, error = $it")
                    parseError(it)
                }
            ).also { disposables += it }
    }

    private fun publishIdentified(email: String, password: String, identified: Boolean) {
        if (identified) {
            login(email, password)
        } else {
            register(email, password)
        }
    }

    private fun setEvent(event: LoginEvent) {
        mutableEvent.value = event
    }
}
