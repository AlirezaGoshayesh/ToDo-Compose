package com.test.todolist.domain.usecases

import com.test.todolist.data.models.ToDoEntry
import com.test.todolist.domain.Repository
import com.test.todolist.domain.base.UseCase
import com.test.todolist.domain.exceptions.IErrorHandler
import com.test.todolist.domain.models.ToDo
import javax.inject.Inject

class EditTodoEntry @Inject constructor(
    private val repository: Repository,
    errorHandler: IErrorHandler
) : UseCase<ToDoEntry, Unit>(errorHandler) {
    override suspend fun execute(parameters: ToDoEntry) {
        return repository.editToDoEntry(parameters)
    }
}