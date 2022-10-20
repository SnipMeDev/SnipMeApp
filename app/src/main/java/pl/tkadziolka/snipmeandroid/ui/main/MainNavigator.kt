package pl.tkadziolka.snipmeandroid.ui.main

import androidx.navigation.NavController
import pl.tkadziolka.snipmeandroid.util.extension.navigateSafe

class MainNavigator {

    fun goToDetail(nav: NavController, snippetId: String) {
        nav.navigateSafe(MainFragmentDirections.goToDetail(snippetId))
    }

    fun goToPreview(nav: NavController, title: String, uuid: String, code: String, language: String) {
        nav.navigateSafe(MainFragmentDirections.goToPreview(title, uuid, code, language))
    }

    fun goToEdit(nav: NavController) {
        nav.navigateSafe(MainFragmentDirections.goToEdit())
    }

    fun goToLogin(nav: NavController) {
        nav.navigateSafe(MainFragmentDirections.goToLogin())
    }

    fun goToError(nav: NavController, message: String?) {
        nav.navigateSafe(MainFragmentDirections.goToError(message))
    }

    fun goToContact(nav: NavController) {
        nav.navigateSafe(MainFragmentDirections.goToContact())
    }

    fun goToDonate(nav: NavController) {
        nav.navigateSafe(MainFragmentDirections.goToDonate())
    }
}