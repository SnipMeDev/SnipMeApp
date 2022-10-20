package pl.tkadziolka.snipmeandroid.ui.detail

import androidx.navigation.NavController
import pl.tkadziolka.snipmeandroid.util.extension.navigateSafe

class DetailNavigator {
    fun goToEdit(nav: NavController, snippetUUID: String) {
        nav.navigateSafe(DetailFragmentDirections.goToEdit(snippetUUID))
    }

    fun goToShare(nav: NavController, snippetUUID: String) {
        nav.navigateSafe(DetailFragmentDirections.goToShare(snippetUUID))
    }

    fun goToLogin(nav: NavController) {
        nav.navigateSafe(DetailFragmentDirections.goToLogin())
    }

    fun goToError(nav: NavController, message: String?) {
        nav.navigateSafe(DetailFragmentDirections.goToError(message))
    }
}