package com.test.todolist.domain.usecases

import com.google.common.truth.Truth.assertThat
import com.test.todolist.data.ErrorHandler
import com.test.todolist.data.FakeRepository
import com.test.todolist.data.models.ToDoCategory
import com.test.todolist.domain.base.Resource
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetTodoCategoriesTest {

    private lateinit var getTodoCategories: GetTodoCategories
    private lateinit var fakeRepository: FakeRepository
    private lateinit var errorHandler: ErrorHandler
    private lateinit var categoriesToAdd: MutableList<ToDoCategory>

    @Before
    fun setup() {
        errorHandler = ErrorHandler()
        fakeRepository = FakeRepository()
        getTodoCategories = GetTodoCategories(fakeRepository, errorHandler)
        categoriesToAdd = mutableListOf()
        ('a'..'z').forEachIndexed { index, c ->
            categoriesToAdd.add(ToDoCategory(name = c.toString(), color = index))
        }
    }

    @Test
    fun `Get categories, returning all`() = runBlocking {
        val res = getTodoCategories(Unit)
        if (res is Resource.Success) {
            assertThat(res.data == categoriesToAdd)
        }
    }

}