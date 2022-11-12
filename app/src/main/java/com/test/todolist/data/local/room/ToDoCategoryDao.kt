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
interface ToDoCategoryDao {
    @Query("SELECT * FROM TODOCATEGORY")
    suspend fun getAllCategories(): List<ToDoCategory>

    @Insert
    suspend fun insert(toDoCategory: ToDoCategory)

    @Update
    suspend fun edit(toDoCategory: ToDoCategory)

    @Delete
    suspend fun delete(toDoCategory: ToDoCategory)
}