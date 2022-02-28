package com.ashour.whipmobilitytest.data.entitities

sealed class Result<out T> {
    data class Successful<out T>(val data: T) : Result<T>()
    abstract class BaseError : Result<Nothing>()
    class AuthenticationError : BaseError()
    data class ClientError(val code: Int) : BaseError()
    class NetworkError : BaseError()
    class ServerError : BaseError()
}