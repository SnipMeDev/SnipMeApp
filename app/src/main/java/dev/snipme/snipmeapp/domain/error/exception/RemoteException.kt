package dev.snipme.snipmeapp.domain.error.exception

class RemoteException(override val cause: Throwable? = null): SnipException()