package com.test.todolist.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.test.todolist.data.models.ToDoCategory
import com.test.todolist.data.models.ToDoEntry
import java.util.Date

@Dao
interface ToDoEntryDao {
    @Query("SELECT * FROM ToDoEntry Where entry_id = :id")
    suspend fun getEntry(id: Int): ToDoEntry

    @Insert
    suspend fun insert(toDoEntry: ToDoEntry)

    @Update
    suspend fun edit(toDoEntry: ToDoEntry)

    @Delete
    suspend fun delete(toDoEntry: ToDoEntry)
}