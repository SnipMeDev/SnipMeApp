package pl.tkadziolka.snipmeandroid.di

import org.koin.dsl.module
import pl.tkadziolka.snipmeandroid.bridge.detail.DetailModel
import pl.tkadziolka.snipmeandroid.bridge.login.LoginModel
import pl.tkadziolka.snipmeandroid.bridge.main.MainModel
import pl.tkadziolka.snipmeandroid.bridge.session.SessionModel

internal val modelModule = module {
    single { SessionModel(get()) }
    single { LoginModel(get(), get(), get()) }
    single { MainModel(get(), get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    single { DetailModel(get(), get(), get(), get(), get(), get(), get(), get()) }
}