package dev.snipme.snipmeapp.domain.message

import android.content.Context
import dev.snipme.snipmeapp.R
import dev.snipme.snipmeapp.domain.error.exception.*

class ErrorMessages(context: Context) {
    private val res = context.resources

    val generic = res.getString(R.string.error_message_generic)
    val noConnection = res.getString(R.string.error_message_no_connection)
    val sessionExpired = res.getString(R.string.error_message_session_expired)
    val noAccess = res.getString(R.string.error_message_no_access)
    val remoteError = res.getString(R.string.error_message_remote_error)
    val unknownLanguage = res.getString(R.string.error_message_unknown_language)
    val alreadyRegistered = res.getString(R.string.error_message_user_already_registered)

    fun parse(throwable: Throwable): String =
        when (throwable) {
            is ConnectionException -> noConnection
            is ContentNotFoundException -> noAccess
            is ForbiddenActionException -> noAccess
            is NetworkNotAvailableException -> noConnection
            is NotAuthorizedException -> sessionExpired
            is RemoteException -> remoteError
            is SessionExpiredException -> sessionExpired
            else -> generic
        }
}