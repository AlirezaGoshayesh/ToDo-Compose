package com.test.todolist.utils

import com.test.todolist.data.models.ToDoCategory
import com.test.todolist.data.models.ToDoEntry

fun Map<ToDoCategory, List<ToDoEntry>>.filterTodayAndConvertToPairs(): List<Pair<ToDoCategory, ToDoEntry>> {
    val map = HashMap(this)
    map.forEach { entry ->
        entry.value.filter { toDoEntry ->
            toDoEntry.date in DateUtils.atStartOfDay(DateUtils.getToday())..DateUtils.atEndOfDay(
                DateUtils.getToday()
            )
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