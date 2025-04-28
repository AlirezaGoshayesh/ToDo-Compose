package com.test.todolist.ui.home

import com.test.todolist.data.models.ToDoEntry
import java.util.*

sealed class TasksIntent {
    object LoadAll : TasksIntent()
    object LoadCategories : TasksIntent()
    data class AddEntry(val categoryId: Int, val title: String, val desc: String, val date: Date) : TasksIntent()
    data class EditEntry(val entry: ToDoEntry) : TasksIntent()
    data class AddCategory(val name: String) : TasksIntent()
    data class DeleteEntry(val entry: ToDoEntry) : TasksIntent()
} 