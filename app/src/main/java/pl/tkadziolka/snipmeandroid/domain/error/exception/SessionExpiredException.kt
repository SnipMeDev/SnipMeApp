package pl.tkadziolka.snipmeandroid.domain.error.exception

class SessionExpiredException(override val cause: Throwable? = null): SnipException()