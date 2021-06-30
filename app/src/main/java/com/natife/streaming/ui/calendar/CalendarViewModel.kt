package com.natife.streaming.ui.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.natife.streaming.R
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.ext.fromCalendar
import com.natife.streaming.preferenses.SettingsPrefs
import com.natife.streaming.router.Router
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.util.*

abstract class CalendarViewModel : BaseViewModel() {
    abstract val date: LiveData<Date>
    abstract fun select(time: LocalDate)
    abstract fun onProceedButtonClicked()

}

class CalendarViewModelImpl(
    private val settingsPrefs: SettingsPrefs,
    private val router: Router,
) : CalendarViewModel() {

    override val date = MutableLiveData<Date>()
//        .apply {
//            value = Date()
//        }

    override fun select(time: LocalDate) {
        settingsPrefs.saveDate(time.fromCalendar()?.time ?: Date().time)
    }

    override fun onProceedButtonClicked() {
        router.navigate(R.id.action_main_homeFragment)
    }

    init {
        val d = settingsPrefs.getDate()
        if (d != null) {
            date.value = Date(d)
        }

        launchCatching {
            withContext(Dispatchers.IO) {
                collect(settingsPrefs.getDateFlow()) {
                    it?.let {
                        date.value = Date(it)
                    }
                }
            }
        }

    }


}