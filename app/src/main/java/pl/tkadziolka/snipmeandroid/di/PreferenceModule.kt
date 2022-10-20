package pl.tkadziolka.snipmeandroid.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import pl.tkadziolka.snipmeandroid.infrastructure.local.AuthPreferences
import pl.tkadziolka.snipmeandroid.util.PreferencesUtil

val preferenceModule = module {
    single { PreferencesUtil(androidContext()) }
    single { AuthPreferences(get()) }
}