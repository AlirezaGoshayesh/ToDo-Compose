package com.test.todolist.ui.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.todolist.data.models.ToDoCategory
import com.test.todolist.data.models.ToDoEntry
import com.test.todolist.domain.base.Resource
import com.test.todolist.domain.models.ToDo
import com.test.todolist.domain.usecases.AddTodoCategory
import com.test.todolist.domain.usecases.AddTodoEntry
import com.test.todolist.domain.usecases.EditTodoEntry
import com.test.todolist.domain.usecases.GetAllTodoEntries
import com.test.todolist.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeVM @Inject constructor(
    private val getAllTodoEntries: GetAllTodoEntries,
    private val editTodoEntry: EditTodoEntry,
    private val addTodoCategory: AddTodoCategory
) : ViewModel() {

    private val _toDoEntries =
        mutableStateOf<Resource<Map<ToDoCategory, List<ToDoEntry>>>>(Resource.Loading)
    val toDoEntries: State<Resource<Map<ToDoCategory, List<ToDoEntry>>>> get() = _toDoEntries

    init {
        getToDoEntries()
    }

    /**
     * Called to get all To Do Entries
     */
    private fun getToDoEntries() {
        viewModelScope.launch {
            _toDoEntries.value = getAllTodoEntries(Unit)
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

}