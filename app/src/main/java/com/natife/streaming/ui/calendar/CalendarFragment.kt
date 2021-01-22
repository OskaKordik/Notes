package com.natife.streaming.ui.calendar

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.core.view.children
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.base.EmptyViewModel
import com.natife.streaming.ext.daysOfWeekFromLocale
import kotlinx.android.synthetic.main.calendar_header.*
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.android.synthetic.main.item_day.*
import kotlinx.android.synthetic.main.item_day.view.*
import java.time.YearMonth
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.*

class CalendarFragment: BaseFragment<EmptyViewModel>() {
    override fun getLayoutRes(): Int = R.layout.fragment_calendar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        class DayViewContainer(view: View) : ViewContainer(view) {
            val textView = view.calendarDayText
            // Will be set when this container is bound

            init {
                view.setOnClickListener {
                    // Use the CalendarDay associated with this container.
                }
            }
        }
        class MonthViewContainer(view: View) : ViewContainer(view) {
            val legendLayout = Example5CalendarHeaderBinding.bind(view).legendLayout.root
        }


        calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                // Set the calendar day for this container.
                // Set the date text
                container.textView.text = day.date.dayOfMonth.toString()
                // Other binding logic
            }
        }

        val currentMonth = YearMonth.now()
        val firstMonth = currentMonth.minusMonths(10)
        val lastMonth = currentMonth.plusMonths(10)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        calendarView.setup(firstMonth, lastMonth, firstDayOfWeek)
        calendarView.scrollToMonth(currentMonth)
    }
}