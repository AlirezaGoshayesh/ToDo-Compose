package com.test.todolist.data

import com.test.todolist.data.models.ToDoCategory
import com.test.todolist.data.models.ToDoEntry
import com.test.todolist.domain.Repository
import java.util.*
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val localDataSource: DataSource.Local
) : Repository {
    override suspend fun getToDoEntries(): Map<ToDoCategory, List<ToDoEntry>> =
        localDataSource.getToDoEntries()

    override suspend fun getToDoEntry(id: Int): ToDoEntry =
        localDataSource.getToDoEntry(id)

    override suspend fun addToDoEntry(
        categoryId: Int,
        title: String,
        desc: String, date: Date
    ) =
        localDataSource.addToDoEntry(categoryId, title, desc, date)

    override suspend fun deleteToDoEntry(toDoEntry: ToDoEntry) =
        localDataSource.deleteToDoEntry(toDoEntry)

    override suspend fun editToDoEntry(toDoEntry: ToDoEntry) =
        localDataSource.editToDoEntry(toDoEntry)

    override suspend fun getToDoCategories(): List<ToDoCategory> =
        localDataSource.getToDoCategories()

    override suspend fun addToDoCategory(name: String, color: Int) =
        localDataSource.addToDoCategory(name, color)

    override suspend fun deleteToDoCategory(toDoCategory: ToDoCategory) =
        localDataSource.deleteToDoCategory(toDoCategory)

    override suspend fun editToDoCategory(toDoCategory: ToDoCategory) =
        localDataSource.editToDoCategory(toDoCategory)

}
