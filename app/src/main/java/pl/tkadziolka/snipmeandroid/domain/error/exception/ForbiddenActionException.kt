package pl.tkadziolka.snipmeandroid.domain.error.exception

class ForbiddenActionException(override val cause: Throwable? = null): SnipException()