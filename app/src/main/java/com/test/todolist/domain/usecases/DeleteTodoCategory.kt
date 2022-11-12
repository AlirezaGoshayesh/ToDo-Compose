package com.test.todolist.domain.usecases

import com.test.todolist.data.models.ToDoCategory
import com.test.todolist.data.models.ToDoEntry
import com.test.todolist.domain.Repository
import com.test.todolist.domain.base.UseCase
import com.test.todolist.domain.exceptions.IErrorHandler
import com.test.todolist.domain.models.ToDo
import javax.inject.Inject

class DeleteTodoCategory @Inject constructor(
    private val repository: Repository,
    errorHandler: IErrorHandler
) : UseCase<ToDoCategory, Unit>(errorHandler) {
    override suspend fun execute(parameters: ToDoCategory) {
        return repository.deleteToDoCategory(parameters)
    }
}