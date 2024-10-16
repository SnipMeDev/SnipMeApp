package dev.snipme.snipmeapp.domain.error.exception

class NetworkNotAvailableException(override val cause: Throwable? = null): SnipException()