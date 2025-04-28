package com.test.todolist.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.todolist.data.models.ToDoCategory
import com.test.todolist.data.models.ToDoEntry
import com.test.todolist.domain.base.Resource
import com.test.todolist.domain.usecases.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TasksVM @Inject constructor(
    private val getAllTodoEntries: GetAllTodoEntries,
    private val editTodoEntry: EditTodoEntry,
    private val addTodoCategory: AddTodoCategory,
    private val getTodoCategories: GetTodoCategories,
    private val addTodoEntry: AddTodoEntry,
    private val deleteTodoEntry: DeleteTodoEntry
) : ViewModel() {

    private val _state = MutableStateFlow<TasksState>(TasksState.Loading)
    val state: StateFlow<TasksState> get() = _state.asStateFlow()

    init {
        processIntent(TasksIntent.LoadAll)
    }

    fun processIntent(intent: TasksIntent) {
        when (intent) {
            is TasksIntent.LoadAll -> {
                loadAll()
            }
            is TasksIntent.LoadCategories -> {
                loadCategories()
            }
            is TasksIntent.AddEntry -> {
                addEntry(intent.categoryId, intent.title, intent.desc, intent.date)
            }
            is TasksIntent.EditEntry -> {
                editEntry(intent.entry)
            }
            is TasksIntent.AddCategory -> {
                addCategory(intent.name)
            }
            is TasksIntent.DeleteEntry -> {
                deleteEntry(intent.entry)
            }
        }
    }

    private fun loadAll() {
        viewModelScope.launch {
            _state.value = TasksState.Loading
            val entriesResult = getAllTodoEntries(Unit)
            val categoriesResult = getTodoCategories(Unit)
            if (entriesResult is Resource.Success && categoriesResult is Resource.Success) {
                _state.value = TasksState.Loaded(
                    entries = entriesResult.data,
                    categories = categoriesResult.data
                )
            } else if (entriesResult is Resource.Error) {
                _state.value = TasksState.Error(entriesResult.errorModel.getErrorMessage())
            } else if (categoriesResult is Resource.Error) {
                _state.value = TasksState.Error(categoriesResult.errorModel.getErrorMessage())
            } else {
                _state.value = TasksState.Error("Unknown error")
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _state.value = TasksState.Loading
            val categoriesResult = getTodoCategories(Unit)
            if (categoriesResult is Resource.Success) {
                val currentEntries = (state.value as? TasksState.Loaded)?.entries ?: emptyMap()
                _state.value = TasksState.Loaded(
                    entries = currentEntries,
                    categories = categoriesResult.data
                )
            } else if (categoriesResult is Resource.Error) {
                _state.value = TasksState.Error(categoriesResult.errorModel.getErrorMessage())
            }
        }
    }

    private fun addEntry(categoryId: Int, title: String, desc: String, date: Date) {
        viewModelScope.launch {
            addTodoEntry(com.test.todolist.domain.models.ToDo(categoryId, title, desc, date))
            processIntent(TasksIntent.LoadAll)
        }
    }

    private fun editEntry(toDoEntry: ToDoEntry) {
        viewModelScope.launch {
            val updatedEntry = toDoEntry.copy(isDone = !toDoEntry.isDone)
            editTodoEntry(updatedEntry)
            processIntent(TasksIntent.LoadAll)
        }
    }

    private fun addCategory(name: String) {
        viewModelScope.launch {
            addTodoCategory(name)
            processIntent(TasksIntent.LoadAll)
        }
    }

    private fun deleteEntry(todoEntry: ToDoEntry) {
        viewModelScope.launch {
            deleteTodoEntry(todoEntry)
            processIntent(TasksIntent.LoadAll)
        }
    }
}