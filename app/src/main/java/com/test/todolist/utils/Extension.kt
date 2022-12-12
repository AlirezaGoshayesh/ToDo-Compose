package com.test.todolist.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.ColorUtils
import com.test.todolist.data.models.ToDoCategory
import com.test.todolist.data.models.ToDoEntry
import java.util.*


fun Map<ToDoCategory, List<ToDoEntry>>.filterDateAndConvertToListOfPairs(
    date: Date? = null
): List<Pair<ToDoCategory, ToDoEntry>> {
    var map = hashMapOf<ToDoCategory, List<ToDoEntry>>()
    if (date != null)
        this.keys.forEach { entry ->
            val list = this[entry]
            if (list != null) {
                map[entry] = list.filter { toDoEntry ->
                    toDoEntry.date in DateUtils.atStartOfDay(date)..DateUtils.atEndOfDay(
                        date
                    )
                }
            }
        } else
        map = this as HashMap<ToDoCategory, List<ToDoEntry>>
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

fun Context.getAppName(): String {
    val stringId = applicationInfo.labelRes
    return if (stringId == 0) applicationInfo.nonLocalizedLabel.toString() else getString(
        stringId
    )
}