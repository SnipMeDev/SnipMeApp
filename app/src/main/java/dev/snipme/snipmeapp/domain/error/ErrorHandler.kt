package dev.snipme.snipmeapp.domain.error

import dev.snipme.snipmeapp.domain.error.exception.SnipException

interface ErrorHandler {
    fun handle(throwable: Throwable): SnipException
}