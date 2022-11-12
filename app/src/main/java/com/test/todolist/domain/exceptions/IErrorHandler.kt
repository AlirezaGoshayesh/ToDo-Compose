package com.test.todolist.domain.exceptions

interface IErrorHandler {
    fun handleException(throwable: Throwable?): ErrorModel
}