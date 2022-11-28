package com.test.todolist.data.models


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class ToDoEntry(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "entry_id") val id: Int = 0,
    @ColumnInfo(name = "category_id_ext") val categoryId: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "desc") val desc: String,
    @ColumnInfo(name = "date") val date: Date,
    @ColumnInfo(name = "isDone") var isDone: Boolean = false
)