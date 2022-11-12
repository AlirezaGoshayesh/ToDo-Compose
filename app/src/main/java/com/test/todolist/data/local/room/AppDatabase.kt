package com.test.todolist.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.test.todolist.data.models.ToDoCategory
import com.test.todolist.data.models.ToDoEntry

@Database(
    entities = [ToDoEntry::class, ToDoCategory::class], version = 1
)

@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun toDoEntryDao(): ToDoEntryDao
    abstract fun toDoCategoryDao(): ToDoCategoryDao
    abstract fun toDoEntryToDoCategoryDao(): ToDoEntryToDoCategoryDao
}