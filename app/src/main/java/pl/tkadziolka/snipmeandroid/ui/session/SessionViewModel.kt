package pl.tkadziolka.snipmeandroid.ui.session

import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import pl.tkadziolka.snipmeandroid.domain.auth.LogoutUserUseCase
import pl.tkadziolka.snipmeandroid.util.extension.inProgress

class SessionViewModel(
    private val logout: LogoutUserUseCase
): ViewModel() {
    private val disposables = CompositeDisposable()
    private var logoutDisposable: Disposable? = null

    fun logOut(onComplete: () -> Unit) {
        if (logoutDisposable.inProgress()) return

        logoutDisposable = logout()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { onComplete() }
            .also { disposables += it }
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}