package com.test.todolist.domain.usecases

import com.test.todolist.domain.Repository
import com.test.todolist.domain.base.UseCase
import com.test.todolist.domain.exceptions.IErrorHandler
import com.test.todolist.domain.models.ToDo
import javax.inject.Inject

class AddTodoEntry @Inject constructor(
    private val repository: Repository,
    errorHandler: IErrorHandler
) : UseCase<ToDo, Unit>(errorHandler) {
    override suspend fun execute(parameters: ToDo) {
        return repository.addToDoEntry(
            parameters.categoryId,
            parameters.title,
            parameters.desc,
            parameters.date
        )
    }
}