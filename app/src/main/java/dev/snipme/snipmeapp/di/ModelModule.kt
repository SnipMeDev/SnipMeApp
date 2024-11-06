package dev.snipme.snipmeapp.di

import org.koin.dsl.module
import dev.snipme.snipmeapp.bridge.detail.DetailModel
import dev.snipme.snipmeapp.bridge.login.LoginModel
import dev.snipme.snipmeapp.bridge.main.MainModel
import dev.snipme.snipmeapp.bridge.session.SessionModel

internal val modelModule = module {
    single { SessionModel(get()) }
    single { LoginModel(get(), get(), get()) }
    single { MainModel(get(), get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    single { DetailModel(get(), get(), get(), get(), get(), get(), get(), get(), get()) }
}