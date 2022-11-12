package com.test.todolist.ui.add

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.todolist.data.models.ToDoCategory
import com.test.todolist.data.models.ToDoEntry
import com.test.todolist.domain.base.Resource
import com.test.todolist.domain.models.ToDo
import com.test.todolist.domain.usecases.AddTodoEntry
import com.test.todolist.domain.usecases.GetAllTodoEntries
import com.test.todolist.domain.usecases.GetTodoCategories
import com.test.todolist.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddVM @Inject constructor(
    private val getTodoCategories: GetTodoCategories,
    private val addTodoEntry: AddTodoEntry
) : ViewModel() {

    private val _toDoCategories =
        mutableStateOf<Resource<List<ToDoCategory>>>(Resource.Loading)
    val toDoCategories: State<Resource<List<ToDoCategory>>> get() = _toDoCategories

    init {
        getCategories()
    }

    /**
     * Called to get all To Do Categories
     */
    private fun getCategories() {
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
            addTodoEntry(ToDo(toDoCategoryId, title, desc, date))
        }

    }
}