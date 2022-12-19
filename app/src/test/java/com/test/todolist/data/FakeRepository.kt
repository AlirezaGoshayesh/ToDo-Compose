package com.test.todolist.data

import com.test.todolist.data.models.ToDoCategory
import com.test.todolist.data.models.ToDoEntry
import com.test.todolist.domain.Repository
import java.util.*

class FakeRepository : Repository {

    private val todoCategories = mutableListOf<ToDoCategory>()
    private val todoEntries = mutableListOf<ToDoEntry>()

    override suspend fun getToDoEntries(): Map<ToDoCategory, List<ToDoEntry>> {
        val map = mutableMapOf<ToDoCategory, List<ToDoEntry>>()
        todoCategories.forEach { toDoCategory ->
            map[toDoCategory] = todoEntries.filter { it.categoryId == toDoCategory.id }
        }
        return map
    }

    override suspend fun getToDoEntry(id: Int): ToDoEntry? = todoEntries.find { it.id == id }

    override suspend fun addToDoEntry(categoryId: Int, title: String, desc: String, date: Date) {
        todoEntries.add(
            ToDoEntry(
                id = todoEntries.size,
                categoryId = categoryId,
                title = title,
                desc = desc,
                date = date
            )
        )
    }

    override suspend fun deleteToDoEntry(toDoEntry: ToDoEntry) {
        todoEntries.remove(toDoEntry)
    }

    override suspend fun editToDoEntry(toDoEntry: ToDoEntry) {
        todoEntries.removeAt(toDoEntry.id)
        todoEntries.add(toDoEntry.id, toDoEntry)
    }

    override suspend fun getToDoCategories(): List<ToDoCategory> {
        return todoCategories
    }

    override suspend fun addToDoCategory(name: String, color: Int) {
        todoCategories.add(ToDoCategory(id = todoCategories.size, name = name, color = color))
    }

    override suspend fun deleteToDoCategory(toDoCategory: ToDoCategory) {
        todoCategories.remove(toDoCategory)
    }

    override suspend fun editToDoCategory(toDoCategory: ToDoCategory) {
        todoCategories.removeAt(toDoCategory.id)
        todoCategories.add(toDoCategory.id, toDoCategory)
    }
}