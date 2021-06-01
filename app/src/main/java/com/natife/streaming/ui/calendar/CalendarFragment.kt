package com.natife.streaming.ui.calendar

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.doOnPreDraw
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.Size
import com.natife.streaming.R
import com.natife.streaming.base.BaseFragment
import com.natife.streaming.ext.daysOfWeekFromLocale
import com.natife.streaming.ext.dp
import com.natife.streaming.ext.fromCalendar
import com.natife.streaming.ext.subscribe
import kotlinx.android.synthetic.main.calendar_header.view.*
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.android.synthetic.main.item_day.view.*
import kotlinx.android.synthetic.main.view_calendar_header.*
import java.time.YearMonth
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.*


class CalendarFragment : BaseFragment<CalendarViewModel>() {
    override fun getLayoutRes(): Int = R.layout.fragment_calendar

    private var currentMonth: YearMonth = YearMonth.now()
    private var firstMonth = currentMonth.minusMonths(0)
    private var lastMonth = currentMonth.plusMonths(0)
    private var firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lang = resources.getString(R.string.lang).toLowerCase(Locale.ROOT)
        include.requestFocus()
        calendarView.doOnPreDraw {
            calendarView.daySize = Size(calendarView.width / 7, (calendarView.height - 40.dp) / 6)
        }

        subscribe(viewModel.date) {
            calendarView.notifyCalendarChanged()
        }

        class DayViewContainer(view: View) : ViewContainer(view) {
            val textView = view.calendarDayText
            val backgroundview = view.dayBackground
        }

        class MonthViewContainer(view: View) : ViewContainer(view) {}

        calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {

                container.textView.text = day.date.dayOfMonth.toString()
                val cal1 = Calendar.getInstance()
                val cal2 = Calendar.getInstance()
                cal1.time = day.date.fromCalendar()
                cal2.time = viewModel.date.value

                if (day.owner != DayOwner.THIS_MONTH) {
                    container.backgroundview.setBackgroundColor(Color.TRANSPARENT)
                    container.textView.setTextColor(Color.TRANSPARENT)
                    container.view.isFocusable = false
                } else if (cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                    cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                ) {
                    container.backgroundview.setBackgroundResource(R.drawable.calendar_current_day)
                } else {
                    container.backgroundview.setBackgroundResource(R.drawable.day_item_background)
                }
                container.view.setOnClickListener {
                    viewModel.select(day.date)
                }
            }
        }
        val daysOfWeek = daysOfWeekFromLocale()
        calendarView.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                val mMonth = when (month.yearMonth.month.getDisplayName(
                    TextStyle.FULL_STANDALONE,
                    Locale(lang)
                ).capitalize(Locale.ROOT)) {
                    "1" -> resources.getString(R.string.january)
                    "2" -> resources.getString(R.string.february)
                    "3" -> resources.getString(R.string.march)
                    "4" -> resources.getString(R.string.april)
                    "5" -> resources.getString(R.string.may)
                    "6" -> resources.getString(R.string.june)
                    "7" -> resources.getString(R.string.july)
                    "8" -> resources.getString(R.string.august)
                    "9" -> resources.getString(R.string.september)
                    "10" -> resources.getString(R.string.october)
                    "11" -> resources.getString(R.string.november)
                    "12" -> resources.getString(R.string.december)
                    else -> month.yearMonth.month.getDisplayName(
                        TextStyle.FULL_STANDALONE,
                        Locale(lang)
                    ).capitalize(Locale.ROOT)
                }
                monthText.text = mMonth
                yearText.text = month.year.toString()

                (container.view.legendLayout as LinearLayout).children.map { it as TextView }
                    .forEachIndexed { index, tv ->
                        tv.text = daysOfWeek[index].getDisplayName(TextStyle.SHORT, Locale(lang))
                            .capitalize()
                    }

            }
        }

        yearLeft.setOnClickListener {
            currentMonth = YearMonth.of(currentMonth.year - 1, currentMonth.month)
            setup()
        }
        yearRight.setOnClickListener {
            currentMonth = YearMonth.of(currentMonth.year + 1, currentMonth.month)
            setup()
        }
        monthLeft.setOnClickListener {
            currentMonth = YearMonth.of(currentMonth.year, currentMonth.month - 1)
            setup()
        }
        monthRight.setOnClickListener {
            currentMonth = YearMonth.of(currentMonth.year, currentMonth.month + 1)
            setup()
        }
        proceed_button.setOnClickListener {
            viewModel.onProceedButtonClicked()
        }
        // start calendar
        setup()
    }

    private fun setup() {
        firstMonth = currentMonth.minusMonths(0)
        lastMonth = currentMonth.plusMonths(0)
        calendarView.setup(firstMonth, lastMonth, firstDayOfWeek)
        calendarView.scrollToMonth(currentMonth)
    }

}