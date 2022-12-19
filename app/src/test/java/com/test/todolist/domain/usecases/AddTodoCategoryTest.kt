package com.test.todolist.domain.usecases

import com.google.common.truth.Truth.assertThat
import com.test.todolist.data.ErrorHandler
import com.test.todolist.data.FakeRepository
import com.test.todolist.domain.base.Resource
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class AddTodoCategoryTest {

    private lateinit var addTodoCategory: AddTodoCategory
    private lateinit var getTodoCategories: GetTodoCategories
    private lateinit var fakeRepository: FakeRepository
    private lateinit var errorHandler: ErrorHandler

    @Before
    fun setup() {
        errorHandler = ErrorHandler()
        fakeRepository = FakeRepository()
        getTodoCategories = GetTodoCategories(fakeRepository, errorHandler)
        addTodoCategory = AddTodoCategory(fakeRepository, errorHandler)
    }

    @Test
    fun `Generate category with random color, random color`() = runBlocking {
        ('a'..'z').forEach { c: Char ->
            addTodoCategory(c.toString())
        }
        val res = getTodoCategories(Unit)
        if (res is Resource.Success) {
            assertThat(res.data.map { it.color }.size == res.data.map { it.color }.distinct().size)
        }
    }
}