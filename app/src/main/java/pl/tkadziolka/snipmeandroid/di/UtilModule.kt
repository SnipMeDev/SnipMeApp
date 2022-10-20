package pl.tkadziolka.snipmeandroid.di

import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import pl.tkadziolka.snipmeandroid.BuildConfig
import pl.tkadziolka.snipmeandroid.domain.error.DebugErrorHandler
import pl.tkadziolka.snipmeandroid.domain.error.SafeErrorHandler
import pl.tkadziolka.snipmeandroid.domain.message.ErrorMessages
import pl.tkadziolka.snipmeandroid.domain.message.ValidationMessages
import pl.tkadziolka.snipmeandroid.domain.message.RealValidationMessages
import pl.tkadziolka.snipmeandroid.ui.Dialogs

internal val utilModule = module {
    factory { if (BuildConfig.DEBUG) DebugErrorHandler() else SafeErrorHandler() }
    factory { ErrorMessages(get()) }
    factory<ValidationMessages> { RealValidationMessages(get()) }
    factory { Dialogs(get()) }
    factory { androidContext().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager }
}