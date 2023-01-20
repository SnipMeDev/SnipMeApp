package pl.tkadziolka.snipmeandroid.di

import org.koin.dsl.module
import pl.tkadziolka.snipmeandroid.ui.detail.DetailNavigator
import pl.tkadziolka.snipmeandroid.ui.login.LoginNavigator
import pl.tkadziolka.snipmeandroid.ui.main.MainNavigator
import pl.tkadziolka.snipmeandroid.ui.splash.SplashNavigator

internal val navigatorModule = module {
    factory { SplashNavigator() }
    factory { LoginNavigator() }
    factory { MainNavigator() }
    factory { DetailNavigator() }
}