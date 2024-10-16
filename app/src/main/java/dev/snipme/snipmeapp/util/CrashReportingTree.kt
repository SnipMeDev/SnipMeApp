package dev.snipme.snipmeapp.util

import timber.log.Timber

class CrashReportingTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        // Don't show logs for user
    }
}