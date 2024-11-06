package dev.snipme.snipmeapp.domain.error.exception

import java.lang.Exception

open class SnipException(override val cause: Throwable? = null): Exception()