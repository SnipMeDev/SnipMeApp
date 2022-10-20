package pl.tkadziolka.snipmeandroid.ui.splash

import androidx.navigation.NavController
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import pl.tkadziolka.snipmeandroid.domain.auth.InitialLoginUseCase
import pl.tkadziolka.snipmeandroid.domain.error.exception.NotAuthorizedException
import pl.tkadziolka.snipmeandroid.ui.viewmodel.StateViewModel
import timber.log.Timber
import java.util.concurrent.TimeUnit

class SplashViewModel(
    private val initialLogin: InitialLoginUseCase,
    private val navigator: SplashNavigator
): StateViewModel<SplashViewState>() {

    override fun init() {
        initialLogin()
            .delay(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = { setState(Logged) },
                onError = {
                    if (it !is NotAuthorizedException)
                        Timber.e("Couldn't get token or user, error = $it")
                    setState(NotAuthorized)
                }
            ).also { disposables += it }
    }

    fun goToLogin(nav: NavController) {
        navigator.goToLogin(nav)
    }

    fun goToMain(nav: NavController) {
        navigator.goToMain(nav)
    }
}

sealed class SplashViewState
object NotAuthorized: SplashViewState()
object Logged: SplashViewState()