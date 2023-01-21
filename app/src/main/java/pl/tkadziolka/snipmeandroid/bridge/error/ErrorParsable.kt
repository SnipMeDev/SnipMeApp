package pl.tkadziolka.snipmeandroid.bridge.error

interface ErrorParsable {
    fun parseError(throwable: Throwable)
}