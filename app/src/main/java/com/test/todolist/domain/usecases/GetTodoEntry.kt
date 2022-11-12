package com.test.todolist.domain.usecases

import com.test.todolist.data.models.ToDoEntry
import com.test.todolist.domain.Repository
import com.test.todolist.domain.base.UseCase
import com.test.todolist.domain.exceptions.IErrorHandler
import javax.inject.Inject

class GetTodoEntry @Inject constructor(
    private val repository: Repository,
    errorHandler: IErrorHandler
) : UseCase<Int, ToDoEntry>(errorHandler) {
    override suspend fun execute(parameters: Int): ToDoEntry {
        return repository.getToDoEntry(parameters)
    }
}