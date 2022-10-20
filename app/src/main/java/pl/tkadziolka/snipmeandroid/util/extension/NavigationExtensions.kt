package pl.tkadziolka.snipmeandroid.util.extension

import androidx.activity.addCallback
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections

fun NavController.navigateUpTo(@IdRes resId: Int) {
    if (currentDestination == null) return // On not null we can't decide
    if (currentDestination!!.id == resId) return // Wanted destination was reached
    navigateUp() // Go back
    navigateUpTo(resId)
}

fun NavController.navigateSafe(navDirections: NavDirections, retryCount: Int = 1) {
    try {
        navigate(navDirections)
    } catch (e: Exception) {
        if (retryCount > 1) navigateSafe(navDirections, retryCount - 1)
    }
}

fun Fragment.onBackPressed(action: () -> Unit) {
    requireActivity().onBackPressedDispatcher.addCallback(this) {
        action()
    }
}