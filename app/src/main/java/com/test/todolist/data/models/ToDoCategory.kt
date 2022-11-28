package com.test.todolist.data.models


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ToDoCategory(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "category_id") val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "color") val color: Int
)