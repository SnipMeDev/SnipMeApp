package pl.tkadziolka.snipmeandroid

import android.app.Application
import io.github.kbiakov.codeview.classifier.CodeProcessor
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.koinApplication
import pl.tkadziolka.snipmeandroid.di.koinModules
import pl.tkadziolka.snipmeandroid.util.CrashReportingTree
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Thread.setDefaultUncaughtExceptionHandler { thread, ex ->
            Timber.e(ex)
        }
        initLogs()
        // train classifier on app start
        CodeProcessor.init(this)
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(koinModules)
        }
    }

    private fun initLogs() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }
    }
}