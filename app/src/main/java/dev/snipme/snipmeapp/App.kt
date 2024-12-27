package dev.snipme.snipmeapp

import android.app.Application
import dev.snipme.snipmeapp.BuildConfig.*
import dev.snipme.snipmeapp.di.koinModules
import dev.snipme.snipmeapp.util.CrashReportingTree
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Thread.setDefaultUncaughtExceptionHandler { thread, ex ->
            Timber.e(ex)
        }
        initLogs()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(koinModules)
        }
    }

    private fun initLogs() {
        if (DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }
    }
}