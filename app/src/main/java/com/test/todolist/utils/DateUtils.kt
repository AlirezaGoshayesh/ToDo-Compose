package com.test.todolist.utils

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.runtime.MutableState
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    fun atEndOfDay(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar[Calendar.HOUR_OF_DAY] = 23
        calendar[Calendar.MINUTE] = 59
        calendar[Calendar.SECOND] = 59
        calendar[Calendar.MILLISECOND] = 999
        return calendar.time
    }

    fun atStartOfDay(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        return calendar.time
    }

    fun makeDatePicker(
        selectedDate: MutableState<Date>,
        context: Context,
        isMinDateToday: Boolean = false,
        isMaxDateToday: Boolean = false
    ): DatePickerDialog {
        val mCalendar = Calendar.getInstance()
        val mYear: Int = mCalendar.get(Calendar.YEAR)
        val mMonth: Int = mCalendar.get(Calendar.MONTH)
        val mDay: Int = mCalendar.get(Calendar.DAY_OF_MONTH)
        val mDatePickerDialog = DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                mCalendar.apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                }
                selectedDate.value = mCalendar.time
            }, mYear, mMonth, mDay
        )
        if (isMinDateToday)
            mDatePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        if (isMaxDateToday)
            mDatePickerDialog.datePicker.maxDate = System.currentTimeMillis() - 1000
        return mDatePickerDialog
    }

    fun calculateDateText(date: Date): String {
        val calendar = Calendar.getInstance()
        calendar.time = date
        val today = Calendar.getInstance()
        val yesterday = Calendar.getInstance()
        yesterday.add(Calendar.DATE, -1)
        return if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == today.get(
                Calendar.DAY_OF_YEAR
            )
        ) {
            "Today's"
        } else if (calendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) && calendar.get(
                Calendar.DAY_OF_YEAR
            ) == yesterday.get(Calendar.DAY_OF_YEAR)
        ) {
            "Yesterday's"
        } else {
            SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).format(date)
        }
    }
}