package pl.tkadziolka.snipmeandroid.ui.error

interface ErrorParsable {
    fun parseError(throwable: Throwable)
}