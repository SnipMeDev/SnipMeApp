package dev.snipme.snipmeapp.domain.error.exception

class ConnectionException(override val cause: Throwable? = null): SnipException()