package pl.tkadziolka.snipmeandroid.domain.error

import pl.tkadziolka.snipmeandroid.domain.error.exception.*
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection.*
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

class SafeErrorHandler : ErrorHandler {

    override fun handle(throwable: Throwable): SnipException {
        if (
            throwable is UnknownHostException ||
            throwable is IOException ||
            throwable is TimeoutException
        ) {
            return ConnectionException()
        }

        if (throwable is HttpException) {
            return when (throwable.code()) {
                HTTP_INTERNAL_ERROR -> RemoteException() // 500
                HTTP_UNAUTHORIZED -> NotAuthorizedException() // 401
                HTTP_NOT_FOUND -> ContentNotFoundException() // 404
                HTTP_FORBIDDEN -> ForbiddenActionException() // 403
                HTTP_BAD_REQUEST -> ForbiddenActionException() // 400
                HTTP_BAD_METHOD -> ForbiddenActionException() // 405
                else -> SnipException()
            }
        }

        return SnipException()
    }
}