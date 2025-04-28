package com.test.todolist.ui.home

import com.test.todolist.data.models.ToDoCategory
import com.test.todolist.data.models.ToDoEntry
import com.test.todolist.domain.base.Resource

sealed class TasksState {
    object Loading : TasksState()
    data class Loaded(
        val entries: Map<ToDoCategory, List<ToDoEntry>> = emptyMap(),
        val categories: List<ToDoCategory> = emptyList()
    ) : TasksState()
    data class Error(val message: String) : TasksState()
} 