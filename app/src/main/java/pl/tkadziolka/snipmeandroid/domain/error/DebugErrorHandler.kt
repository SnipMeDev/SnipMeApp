package pl.tkadziolka.snipmeandroid.domain.error

import pl.tkadziolka.snipmeandroid.domain.error.exception.*
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.net.HttpURLConnection
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

class DebugErrorHandler: ErrorHandler {

    override fun handle(throwable: Throwable): SnipException {
        Timber.e(throwable)

        if (
            throwable is UnknownHostException ||
            throwable is IOException ||
            throwable is TimeoutException
        ) {
            return ConnectionException(throwable)
        }

        if (throwable is HttpException) {
            return when (throwable.code()) {
                HttpURLConnection.HTTP_INTERNAL_ERROR -> RemoteException(throwable) // 500
                HttpURLConnection.HTTP_UNAUTHORIZED -> NotAuthorizedException(throwable) // 401
                HttpURLConnection.HTTP_NOT_FOUND -> ContentNotFoundException(throwable) // 404
                HttpURLConnection.HTTP_FORBIDDEN -> ForbiddenActionException(throwable) // 403
                HttpURLConnection.HTTP_BAD_REQUEST -> ForbiddenActionException(throwable) // 400
                HttpURLConnection.HTTP_BAD_METHOD -> ForbiddenActionException(throwable) // 405
                else -> SnipException(throwable)
            }
        }

        return SnipException(throwable)
    }
}