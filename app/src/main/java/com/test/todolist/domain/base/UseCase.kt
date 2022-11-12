package com.test.todolist.domain.base

import com.test.todolist.domain.exceptions.IErrorHandler
import kotlin.coroutines.cancellation.CancellationException

abstract class UseCase<in P, R>(private val errorHandler: IErrorHandler) {

    /** Executes the use case asynchronously and returns a [Resource].
     *
     * @return a [Resource].
     *
     * @param parameters the input parameters to run the use case with
     */

    suspend operator fun invoke(parameters: P): Resource<R> {
        return try {
            execute(parameters).let {
                println("$TAG Response: $it")
                Resource.Success(it)
            }
        } catch (e: CancellationException) {
            println("$TAG Error: $e")
            Resource.Error(errorHandler.handleException(e))
        } catch (e: Exception) {
            println("$TAG Error:$e cause: ${e.cause}")
            Resource.Error(errorHandler.handleException(e))
        }
    }

    /**
     * Override this to set the code to be executed.
     */
    @Throws(RuntimeException::class)
    protected abstract suspend fun execute(parameters: P): R

    companion object {
        private val TAG = UseCase::class.java.name
    }
}