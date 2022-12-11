package com.test.todolist.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.todolist.data.models.ToDoCategory
import com.test.todolist.data.models.ToDoEntry
import com.test.todolist.domain.base.Resource
import com.test.todolist.domain.usecases.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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

    private val _toDoEntries =
        MutableStateFlow<Resource<Map<ToDoCategory, List<ToDoEntry>>>>(Resource.Loading)
    val toDoEntries: StateFlow<Resource<Map<ToDoCategory, List<ToDoEntry>>>> get() = _toDoEntries.asStateFlow()

    private val _toDoCategories =
        MutableStateFlow<Resource<List<ToDoCategory>>>(Resource.Loading)
    val toDoCategories: StateFlow<Resource<List<ToDoCategory>>> get() = _toDoCategories.asStateFlow()

    init {
        getToDoEntries()
    }

    /**
     * Called to get all To Do Entries
     */
    private fun getToDoEntries() {
        viewModelScope.launch {
            _toDoEntries.value = Resource.Loading
            delay(500)
            _toDoEntries.value = getAllTodoEntries(Unit)
        }

    }

    /**
     * Called to get all To Do Categories
     */
    fun getCategories() {
        viewModelScope.launch {
            _toDoCategories.value = getTodoCategories(Unit)
        }

    }

    /**
     * Called to add a To Do Entries
     */
    fun addToDoEntry(
        toDoCategoryId: Int,
        title: String,
        desc: String,
        date: Date
    ) {
        viewModelScope.launch {
            addTodoEntry(com.test.todolist.domain.models.ToDo(toDoCategoryId, title, desc, date))
            getToDoEntries()
        }

    }

    /**
     * Called to edit To Do Entry
     */
    fun editToDoEntry(toDoEntry: ToDoEntry) {
        viewModelScope.launch {
            toDoEntry.isDone = !toDoEntry.isDone
            editTodoEntry(toDoEntry)
            getToDoEntries()
        }
    }

    /**
     * Called to add To Do Category
     */
    fun addToDoCategory(name: String) {
        viewModelScope.launch {
            addTodoCategory(name)
            getToDoEntries()
        }
    }

    /**
     * Called to delete To Do Entry
     */
    fun deleteTodo(todoEntry: ToDoEntry) {
        viewModelScope.launch {
            deleteTodoEntry(todoEntry)
            getToDoEntries()
        }
    }

}