package com.test.todolist.data

import com.test.todolist.data.models.ToDoCategory
import com.test.todolist.data.models.ToDoEntry
import java.util.Date

interface DataSource {

    interface Local {

        suspend fun getToDoEntries(): Map<ToDoCategory, List<ToDoEntry>>

        suspend fun getToDoEntry(id: Int): ToDoEntry

        suspend fun addToDoEntry(
            categoryId: Int,
            title: String,
            desc: String,
            date: Date
        )

        suspend fun deleteToDoEntry(toDoEntry: ToDoEntry)

        suspend fun editToDoEntry(toDoEntry: ToDoEntry)

        suspend fun getToDoCategories(): List<ToDoCategory>

        suspend fun addToDoCategory(name: String, color: Int)

        suspend fun deleteToDoCategory(toDoCategory: ToDoCategory)

        suspend fun editToDoCategory(toDoCategory: ToDoCategory)

    }

}