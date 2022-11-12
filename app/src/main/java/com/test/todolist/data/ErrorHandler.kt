package com.test.todolist.data

import com.test.todolist.domain.exceptions.ErrorModel
import com.test.todolist.domain.exceptions.IErrorHandler
import java.io.IOException
import java.net.SocketTimeoutException
import kotlin.coroutines.cancellation.CancellationException

class ErrorHandler : IErrorHandler {

    override fun handleException(throwable: Throwable?): ErrorModel {
        val errorModel: ErrorModel? = when (throwable) {

            // if throwable is an instance of HttpException
            // then attempt to parse error data from response body
            is CancellationException -> {
                ErrorModel(
                    "Canceled by user!",
                    0,
                    ErrorModel.ErrorStatus.CANCELED
                )
            }

            // handle api call timeout error
            is SocketTimeoutException -> {
                ErrorModel(
                    throwable.message,
                    ErrorModel.ErrorStatus.TIMEOUT
                )
            }

            // handle connection error
            is IOException -> {
                ErrorModel(
                    throwable.message,
                    ErrorModel.ErrorStatus.NO_CONNECTION
                )
            }
            else -> null
        }
        return errorModel ?: ErrorModel(
            "No Defined Error!",
            0,
            ErrorModel.ErrorStatus.BAD_RESPONSE
        )
    }

}
