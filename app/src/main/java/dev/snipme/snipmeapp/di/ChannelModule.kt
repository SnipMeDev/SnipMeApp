package dev.snipme.snipmeapp.di

import dev.snipme.snipmeapp.bridge.FlowChannelEventStreamHandler
import dev.snipme.snipmeapp.bridge.FlowChannelStateStreamHandler
import dev.snipme.snipmeapp.bridge.Details.DetailsModel
import dev.snipme.snipmeapp.bridge.login.LoginModel
import dev.snipme.snipmeapp.bridge.main.MainModel
import dev.snipme.snipmeapp.bridge.session.SessionModel
import org.koin.dsl.module

internal val channelModule = module {
    single { FlowChannelStateStreamHandler() }
    single { FlowChannelEventStreamHandler() }
    single { SessionModel(get()) }
    single { LoginModel(get(), get(), get()) }
    single { MainModel(get(), get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    single { DetailsModel(get(), get(), get(), get(), get(), get(), get(), get(), get()) }
}