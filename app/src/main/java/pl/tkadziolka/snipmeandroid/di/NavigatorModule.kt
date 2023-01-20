package pl.tkadziolka.snipmeandroid.di

import org.koin.dsl.module
import pl.tkadziolka.snipmeandroid.ui.splash.SplashNavigator

internal val navigatorModule = module {
    factory { SplashNavigator() }
}