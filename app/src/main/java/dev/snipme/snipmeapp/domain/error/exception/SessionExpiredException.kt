package dev.snipme.snipmeapp.domain.error.exception

class SessionExpiredException(override val cause: Throwable? = null): SnipException()