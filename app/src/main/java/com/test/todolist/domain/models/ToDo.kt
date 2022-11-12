package com.test.todolist.domain.models

import java.util.Date

data class ToDo(
    var categoryId: Int,
    val title: String,
    val desc: String,
    val date: Date,
    var isDone: Boolean = false
)
