package com.vorobyoff.gallery.data.exceptions

import com.vorobyoff.gallery.data.exceptions.NetworkErrorException.ClientErrorException
import com.vorobyoff.gallery.data.exceptions.NetworkErrorException.ServerErrorException
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException

fun catchNetworkException(exception: Exception): Exception = when (exception) {
    is IOException, is UnknownHostException -> ConnectionException()
    is HttpException -> exceptionFromErrorCode(exception.code(), exception.message())
    else -> GenericException
}

private fun exceptionFromErrorCode(errorCode: Int, message: String): Exception =
    when (errorCode) {
        in 400..499 -> ClientErrorException(message = message, errorCode = errorCode)
        in 500..526 -> ServerErrorException(message = message, errorCode = errorCode)
        else -> GenericException
    }