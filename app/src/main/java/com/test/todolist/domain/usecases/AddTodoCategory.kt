package com.test.todolist.domain.usecases

import android.graphics.Color
import com.test.todolist.domain.Repository
import com.test.todolist.domain.base.UseCase
import com.test.todolist.domain.exceptions.IErrorHandler
import javax.inject.Inject

class AddTodoCategory @Inject constructor(
    private val repository: Repository,
    errorHandler: IErrorHandler
) : UseCase<String, Unit>(errorHandler) {
    override suspend fun execute(parameters: String) {
        val color = Color.argb(255, (0..255).random(), (0..255).random(), (0..255).random())
        return repository.addToDoCategory(parameters, color)
    }
}