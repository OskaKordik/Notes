package com.natife.streaming.ext

import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.temporal.WeekFields
import java.util.*

fun Date.toRequest(): String{
    val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault())
    return sdf.format(this)
}
fun Date.toDisplay(): String{
    val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    return sdf.format(this)
}

fun daysOfWeekFromLocale(): Array<DayOfWeek> {
    val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
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