package pl.tkadziolka.snipmeandroid.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle

const val STATE_KEY = "state"

abstract class PersistedStateViewModel<T>: StateViewModel<T>() {
    protected val stateHandle = SavedStateHandle()

    final override val mutableState = stateHandle.getLiveData<T>(STATE_KEY)
    override val state: LiveData<T> = mutableState

    override fun init() {
        if (stateHandle.contains(STATE_KEY).not())
            initState()
    }

    abstract fun initState()
}