package dev.snipme.snipmeapp.di

import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import dev.snipme.snipmeapp.BuildConfig
import dev.snipme.snipmeapp.domain.error.DebugErrorHandler
import dev.snipme.snipmeapp.domain.error.SafeErrorHandler
import dev.snipme.snipmeapp.domain.message.ErrorMessages
import dev.snipme.snipmeapp.domain.message.ValidationMessages
import dev.snipme.snipmeapp.domain.message.RealValidationMessages

internal val utilModule = module {
    factory { if (BuildConfig.DEBUG) DebugErrorHandler() else SafeErrorHandler() }
    factory { ErrorMessages(get()) }
    factory<ValidationMessages> { RealValidationMessages(get()) }
    single { androidApplication().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager }
}