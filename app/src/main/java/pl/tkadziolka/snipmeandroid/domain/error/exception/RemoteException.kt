package pl.tkadziolka.snipmeandroid.domain.error.exception

class RemoteException(override val cause: Throwable? = null): SnipException()