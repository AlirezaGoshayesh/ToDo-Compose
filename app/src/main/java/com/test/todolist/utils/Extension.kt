package com.test.todolist.utils

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.ColorUtils
import com.test.todolist.data.models.ToDoCategory
import com.test.todolist.data.models.ToDoEntry
import java.util.Date

fun Map<ToDoCategory, List<ToDoEntry>>.filterDayAndConvertToPairs(date: Date): List<Pair<ToDoCategory, ToDoEntry>> {
    val map = hashMapOf<ToDoCategory, List<ToDoEntry>>()
    this.keys.forEach { entry ->
        val list = this[entry]
        if (list != null) {
            map[entry] = list.filter { toDoEntry ->
                toDoEntry.date in DateUtils.atStartOfDay(date)..DateUtils.atEndOfDay(
                    date
                )
            }
        }
    }
    val list = mutableListOf<Pair<ToDoCategory, ToDoEntry>>()
    map.forEach { (key, value) ->
        value.forEach { todo ->
            list.add(Pair(key, todo))
        }
    }
    return list.sortedBy { it.second.date }
}

fun Int.getContrastColor(): Color {
    val contrast = ColorUtils.calculateContrast(android.graphics.Color.WHITE, this)
    return if (contrast > 1.5)
        Color.White else Color.Black
}