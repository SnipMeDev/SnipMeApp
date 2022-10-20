package pl.tkadziolka.snipmeandroid.domain.error.exception

class NotAuthorizedException(override val cause: Throwable? = null): SnipException()