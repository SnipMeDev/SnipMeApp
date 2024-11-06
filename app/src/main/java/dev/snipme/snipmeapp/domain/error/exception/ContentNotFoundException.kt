package dev.snipme.snipmeapp.domain.error.exception

class ContentNotFoundException(override val cause: Throwable? = null): SnipException()