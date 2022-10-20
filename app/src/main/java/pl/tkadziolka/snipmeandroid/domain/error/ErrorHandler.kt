package pl.tkadziolka.snipmeandroid.domain.error

import pl.tkadziolka.snipmeandroid.domain.error.exception.SnipException

interface ErrorHandler {
    fun handle(throwable: Throwable): SnipException
}