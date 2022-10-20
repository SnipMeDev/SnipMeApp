package pl.tkadziolka.snipmeandroid.util.view

import android.os.Bundle
import android.os.Parcelable

private const val SUPER_STATE_KEY = "super_state_key"

interface SavedStateView {

    val onSaveState: (Bundle) -> Bundle

    val onRestoreState: (Bundle) -> Unit

    // Call as onSaveInstance(super.onSaveInstanceState())
    fun onSaveInstance(superState: Parcelable?): Parcelable {
        val bundle = Bundle()
        bundle.putParcelable(SUPER_STATE_KEY, superState)
        return onSaveState(bundle)
    }

    // Call as super.onRestoreInstanceState(onRestoreInstance(state))
    fun onRestoreInstance(state: Parcelable?): Parcelable? {
        var newState = state
        if (state is Bundle) {
            onRestoreState(state)
            newState = state.getParcelable(SUPER_STATE_KEY)
        }
        return newState
    }
}