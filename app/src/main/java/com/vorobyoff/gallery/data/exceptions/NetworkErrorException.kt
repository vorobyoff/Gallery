package com.vorobyoff.gallery.data.exceptions

sealed class NetworkErrorException(override val message: String, open val errorCode: Int) : Exception() {
    class ClientErrorException(override val message: String, errorCode: Int) :
        NetworkErrorException(message, errorCode)

    class ServerErrorException(override val message: String, errorCode: Int) :
        NetworkErrorException(message, errorCode)
}