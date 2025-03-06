package dev.snipme.snipmeapp.channel.error

interface ErrorParsable {
    fun parseError(throwable: Throwable)
}