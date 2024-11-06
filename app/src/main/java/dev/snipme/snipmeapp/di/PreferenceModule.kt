package dev.snipme.snipmeapp.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import dev.snipme.snipmeapp.infrastructure.local.AuthPreferences
import dev.snipme.snipmeapp.util.PreferencesUtil

val preferenceModule = module {
    single { PreferencesUtil(androidContext()) }
    single { AuthPreferences(get()) }
}