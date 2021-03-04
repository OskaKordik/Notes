package com.natife.streaming.ext

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.*

fun Date.toRequest(): String {
    val sdf0 = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
    return sdf.format(sdf0.parse(sdf0.format(this)))
}

fun String.fromResponse(): Date {
    val sdf = SimpleDateFormat("yyyy-MM-ddHH:mm:ss'Z'", Locale.getDefault())
    return sdf.parse(this)
}

fun Date.toDisplay(): String {
    val sdf = SimpleDateFormat("dd MMMM yyyy", Locale("ru"))//TODO multilang
    return sdf.format(this)
}
fun Date.toDisplay2(): String {
    val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale("ru"))//TODO multilang
    return sdf.format(this)
}

fun LocalDate.fromCalendar(): Date? {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sdf.parse(this.toString())
}

fun Long.toDate(): Date {
    return Date(this)
}

fun Long.toDisplayTime(): String {
    val miliss = this*1000
    val sdf1 = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    val sdf2 = SimpleDateFormat("mm:ss", Locale.getDefault())
    val sdf3 = SimpleDateFormat("ss", Locale.getDefault())
    return when {
        miliss < 1000 * 60 -> sdf2.format(Date(miliss))
        miliss in 1000 * 60..1000 * 60 * 60 -> sdf2.format(Date(miliss))
        else -> sdf1.format(Date(miliss))
    }


}

fun daysOfWeekFromLocale(): Array<DayOfWeek> {
    val firstDayOfWeek = WeekFields.of(Locale("ru")).firstDayOfWeek
    var daysOfWeek = DayOfWeek.values()
    // Order `daysOfWeek` array so that firstDayOfWeek is at index 0.
    // Only necessary if firstDayOfWeek != DayOfWeek.MONDAY which has ordinal 0.
    if (firstDayOfWeek != DayOfWeek.MONDAY) {
        val rhs = daysOfWeek.sliceArray(firstDayOfWeek.ordinal..daysOfWeek.indices.last)
        val lhs = daysOfWeek.sliceArray(0 until firstDayOfWeek.ordinal)
        daysOfWeek = rhs + lhs
    }
    return daysOfWeek
}