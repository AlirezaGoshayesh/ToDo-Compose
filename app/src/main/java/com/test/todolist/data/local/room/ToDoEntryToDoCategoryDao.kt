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
interface ToDoEntryToDoCategoryDao {
    @Query(
        "SELECT * FROM TODOCATEGORY" + " LEFT JOIN ToDoEntry ON TODOCATEGORY.category_id = ToDoEntry.category_id_ext"
    )
    suspend fun getAll(): Map<ToDoCategory, List<ToDoEntry>>

}