package com.test.todolist.domain

import com.test.todolist.data.models.ToDoCategory
import com.test.todolist.data.models.ToDoEntry
import com.test.todolist.domain.models.ToDo
import java.util.*


interface Repository {

    suspend fun getToDoEntries(): Map<ToDoCategory, List<ToDoEntry>>

    suspend fun getToDoEntry(id: Int): ToDoEntry

    suspend fun addToDoEntry(
        categoryId: Int,
        title: String,
        desc: String, date: Date
    )

    suspend fun deleteToDoEntry(toDoEntry: ToDoEntry)

    suspend fun editToDoEntry(toDoEntry: ToDoEntry)

    suspend fun getToDoCategories(): List<ToDoCategory>

    suspend fun addToDoCategory(name: String, color: Int)

    suspend fun deleteToDoCategory(toDoCategory: ToDoCategory)

    suspend fun editToDoCategory(toDoCategory: ToDoCategory)

}
