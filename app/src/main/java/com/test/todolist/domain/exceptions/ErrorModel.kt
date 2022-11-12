package com.test.todolist.domain.exceptions

private const val NO_CONNECTION_ERROR_MESSAGE = "NO_CONNECTION_ERROR_MESSAGE"
private const val BAD_RESPONSE_ERROR_MESSAGE = "BAD_RESPONSE_ERROR_MESSAGE"
private const val TIME_OUT_ERROR_MESSAGE = "TIME_OUT_ERROR_MESSAGE"
private const val EMPTY_RESPONSE_ERROR_MESSAGE = "EMPTY_RESPONSE_ERROR_MESSAGE"
private const val NOT_DEFINED_ERROR_MESSAGE = "NOT_DEFINED_ERROR_MESSAGE"
private const val UNAUTHORIZED_ERROR_MESSAGE = "UNAUTHORIZED_ERROR_MESSAGE"
private const val CANCELED_BY_USER = "CANCELED_BY_USER"
private const val TOO_MANY_ATTEMPTS = "TOO_MANY_ATTEMPTS"

data class ErrorModel(
    val message: String?,
    val code: Int?,
    var errorStatus: ErrorStatus
) {

    constructor(errorStatus: ErrorStatus) : this(null, null, errorStatus)

    constructor(message: String?, errorStatus: ErrorStatus) : this(message, null, errorStatus)

    fun getErrorMessage(): String {
        return when (errorStatus) {
            ErrorStatus.NO_CONNECTION -> NO_CONNECTION_ERROR_MESSAGE
            ErrorStatus.BAD_RESPONSE -> BAD_RESPONSE_ERROR_MESSAGE
            ErrorStatus.TIMEOUT -> TIME_OUT_ERROR_MESSAGE
            ErrorStatus.EMPTY_RESPONSE -> EMPTY_RESPONSE_ERROR_MESSAGE
            ErrorStatus.NOT_DEFINED -> NOT_DEFINED_ERROR_MESSAGE
            ErrorStatus.UNAUTHORIZED -> UNAUTHORIZED_ERROR_MESSAGE
            ErrorStatus.CANCELED -> CANCELED_BY_USER
            ErrorStatus.TOO_MANY_ATTEMPTS -> TOO_MANY_ATTEMPTS
        }
    }

    enum class ErrorStatus {
        /**
         * error in connecting to repository (Server or Database)
         */
        NO_CONNECTION,

        /**
         * error in getting value (Json Error, Server Error, etc)
         */
        BAD_RESPONSE,

        /**
         * Time out  error
         */
        TIMEOUT,

        /**
         * no data available in repository
         */
        EMPTY_RESPONSE,

        /**
         * an unexpected error
         */
        NOT_DEFINED,

        /**
         * bad credential
         */
        UNAUTHORIZED,

        /**
         * canceled by user
         */
        CANCELED,

        /**
         * too many requests
         */
        TOO_MANY_ATTEMPTS,

    }
}