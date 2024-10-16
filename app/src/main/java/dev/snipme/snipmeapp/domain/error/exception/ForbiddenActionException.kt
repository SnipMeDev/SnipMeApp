package dev.snipme.snipmeapp.domain.error.exception

class ForbiddenActionException(override val cause: Throwable? = null): SnipException()