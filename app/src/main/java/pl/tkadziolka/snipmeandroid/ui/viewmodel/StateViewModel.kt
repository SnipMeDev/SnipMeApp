package pl.tkadziolka.snipmeandroid.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

@Suppress("ImplicitThis")
abstract class StateViewModel<T> : ViewModel() {
    protected val disposables = CompositeDisposable()

    protected open val mutableState = MutableLiveData<T>()
    open val state: LiveData<T> = mutableState

    open fun init() = Unit

    fun setState(newState: T) {
        newState?.let { mutableState.value = it }
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}