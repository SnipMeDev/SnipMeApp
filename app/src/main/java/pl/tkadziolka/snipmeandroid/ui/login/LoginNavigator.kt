package pl.tkadziolka.snipmeandroid.ui.login

import androidx.navigation.NavController
import pl.tkadziolka.snipmeandroid.util.extension.navigateSafe

class LoginNavigator {

    fun goToMain(navController: NavController) {
        navController.navigateSafe(LoginFragmentDirections.goToMain())
    }

    fun goToError(nav: NavController, message: String?) {
        nav.navigateSafe(LoginFragmentDirections.goToError(message))
    }
}