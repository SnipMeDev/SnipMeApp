package dev.snipme.snipmeapp.di

import dev.snipme.snipmeapp.channel.FlowChannelEventStreamHandler
import dev.snipme.snipmeapp.channel.FlowChannelStateStreamHandler
import dev.snipme.snipmeapp.channel.details.DetailsModel
import dev.snipme.snipmeapp.channel.login.LoginModel
import dev.snipme.snipmeapp.channel.main.MainModel
import dev.snipme.snipmeapp.channel.session.SessionModel
import org.koin.dsl.module

internal val channelModule = module {
    single { FlowChannelStateStreamHandler() }
    single { FlowChannelEventStreamHandler() }
    single { SessionModel(get()) }
    single { LoginModel(get(), get(), get()) }
    single { MainModel(get(), get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    single { DetailsModel(get(), get(), get(), get(), get(), get(), get(), get()) }
}