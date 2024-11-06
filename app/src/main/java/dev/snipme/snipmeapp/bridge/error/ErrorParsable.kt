package dev.snipme.snipmeapp.bridge.error

interface ErrorParsable {
    fun parseError(throwable: Throwable)
}