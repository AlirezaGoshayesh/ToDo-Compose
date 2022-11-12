package com.test.todolist.data.local

import com.test.todolist.data.DataSource
import com.test.todolist.data.local.room.ToDoCategoryDao
import com.test.todolist.data.local.room.ToDoEntryDao
import com.test.todolist.data.local.room.ToDoEntryToDoCategoryDao
import com.test.todolist.data.models.ToDoCategory
import com.test.todolist.data.models.ToDoEntry
import java.util.*
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val toDoEntryDao: ToDoEntryDao,
    private val toDoCategoryDao: ToDoCategoryDao,
    private val toDoEntryToDoCategoryDao: ToDoEntryToDoCategoryDao
) : DataSource.Local {
    override suspend fun getToDoEntries(): Map<ToDoCategory, List<ToDoEntry>> =
        toDoEntryToDoCategoryDao.getAll()

    override suspend fun getToDoEntry(id: Int): ToDoEntry = toDoEntryDao.getEntry(id)

    override suspend fun addToDoEntry(categoryId: Int, title: String, desc: String, date: Date) =
        toDoEntryDao.insert(
            ToDoEntry(
                categoryId = categoryId,
                title = title,
                desc = desc,
                date = date
            )
        )

    override suspend fun deleteToDoEntry(toDoEntry: ToDoEntry) = toDoEntryDao.delete(toDoEntry)

    override suspend fun editToDoEntry(toDoEntry: ToDoEntry) = toDoEntryDao.edit(toDoEntry)

    override suspend fun getToDoCategories(): List<ToDoCategory> =
        toDoCategoryDao.getAllCategories()

    override suspend fun addToDoCategory(name: String, color: Int) =
        toDoCategoryDao.insert(ToDoCategory(name = name, color = color))

    override suspend fun deleteToDoCategory(toDoCategory: ToDoCategory) =
        toDoCategoryDao.delete(toDoCategory)

    override suspend fun editToDoCategory(toDoCategory: ToDoCategory) =
        toDoCategoryDao.edit(toDoCategory)


}
