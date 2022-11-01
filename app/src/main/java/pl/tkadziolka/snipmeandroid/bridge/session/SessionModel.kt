package pl.tkadziolka.snipmeandroid.bridge.session

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import pl.tkadziolka.snipmeandroid.domain.auth.LogoutUserUseCase
import pl.tkadziolka.snipmeandroid.util.extension.inProgress

class SessionModel(
    private val logout: LogoutUserUseCase
) {
    private val disposables = CompositeDisposable()
    private var logoutDisposable: Disposable? = null

    fun logOut(onComplete: () -> Unit) {
        if (logoutDisposable.inProgress()) return

        logoutDisposable = logout()
            .subscribeOn(Schedulers.io())
            .subscribe { onComplete() }
            .also { disposables += it }
    }
}