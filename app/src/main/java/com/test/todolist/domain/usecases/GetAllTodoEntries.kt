package com.test.todolist.domain.usecases

import com.test.todolist.data.models.ToDoCategory
import com.test.todolist.data.models.ToDoEntry
import com.test.todolist.domain.Repository
import com.test.todolist.domain.base.UseCase
import com.test.todolist.domain.exceptions.IErrorHandler
import javax.inject.Inject

class GetAllTodoEntries @Inject constructor(
    private val repository: Repository,
    errorHandler: IErrorHandler
) : UseCase<Unit, Map<ToDoCategory, List<ToDoEntry>>>(errorHandler) {
    override suspend fun execute(parameters: Unit): Map<ToDoCategory, List<ToDoEntry>> {
        return repository.getToDoEntries()
    }
}