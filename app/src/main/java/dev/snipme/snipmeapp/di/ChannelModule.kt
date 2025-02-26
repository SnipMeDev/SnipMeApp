package dev.snipme.snipmeapp.di

import dev.snipme.snipmeapp.bridge.FlowChannelStateStreamHandler
import org.koin.dsl.module
import dev.snipme.snipmeapp.bridge.detail.DetailModel
import dev.snipme.snipmeapp.bridge.login.LoginModel
import dev.snipme.snipmeapp.bridge.main.MainModel
import dev.snipme.snipmeapp.bridge.session.SessionModel

internal val channelModule = module {
    single { FlowChannelStateStreamHandler() }
    single { SessionModel(get()) }
    single { LoginModel(get(), get(), get()) }
    single { MainModel(get(), get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    single { DetailModel(get(), get(), get(), get(), get(), get(), get(), get(), get()) }
}