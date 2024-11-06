package dev.snipme.snipmeapp.bridge.session

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import dev.snipme.snipmeapp.domain.auth.LogoutUserUseCase
import dev.snipme.snipmeapp.util.extension.inProgress

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