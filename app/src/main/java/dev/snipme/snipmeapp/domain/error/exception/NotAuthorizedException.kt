package dev.snipme.snipmeapp.domain.error.exception

class NotAuthorizedException(override val cause: Throwable? = null): SnipException()