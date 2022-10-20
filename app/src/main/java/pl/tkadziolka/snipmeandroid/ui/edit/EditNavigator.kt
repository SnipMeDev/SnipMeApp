package pl.tkadziolka.snipmeandroid.ui.edit

import androidx.navigation.NavController
import pl.tkadziolka.snipmeandroid.util.extension.navigateSafe

class EditNavigator {

    fun goToDetail(nav: NavController, snippetId: String) {
        nav.navigateSafe(EditFragmentDirections.goToDetail(snippetId))
    }

    fun goToLogin(nav: NavController) {
        nav.navigateSafe(EditFragmentDirections.goToLogin())
    }

    fun goToError(nav: NavController, message: String?) {
        nav.navigateSafe(EditFragmentDirections.goToError(message))
    }
}