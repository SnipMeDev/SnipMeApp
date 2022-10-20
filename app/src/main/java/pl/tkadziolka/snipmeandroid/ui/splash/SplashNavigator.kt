package pl.tkadziolka.snipmeandroid.ui.splash

import androidx.navigation.NavController
import pl.tkadziolka.snipmeandroid.util.extension.navigateSafe

class SplashNavigator {

    fun goToLogin(nav: NavController) {
        nav.navigateSafe(SplashFragmentDirections.goToLogin())
    }

    fun goToMain(nav: NavController) {
        nav.navigateSafe(SplashFragmentDirections.goToMain())
    }
}