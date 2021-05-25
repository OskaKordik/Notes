package com.natife.streaming.ui.main

import androidx.lifecycle.MutableLiveData
import com.natife.streaming.R
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.ext.toDate
import com.natife.streaming.preferenses.AuthPrefs
import com.natife.streaming.preferenses.SettingsPrefs
import com.natife.streaming.router.Router
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class MainViewModel(
    private val authPrefs: AuthPrefs,
    private val router: Router,
    private val settingsPrefs: SettingsPrefs
) : BaseViewModel() {
    val name = MutableLiveData<String>()
    val date = MutableLiveData<Date>()
    fun back(){
        router.navigateUp()
    }
    init {
        launch {
            val isLoggedIn = authPrefs.isLoggedIn()
            if (isLoggedIn) {
                router.navigate(R.id.action_global_nav_main)
            } else {
                router.navigate(R.id.action_global_nav_auth)
            }
        }
        launch {
            collect(authPrefs.getProfileFlow()) {

                name.value = "${it?.firstName ?: ""} ${it?.lastName ?: ""}"
            }
        }

        val date = settingsPrefs.getDate()
        if (date == null) {
            this.date.value = Date()
        } else {
            this.date.value = Date(date)
        }
        launchCatching {
            withContext(Dispatchers.IO) {
                collect(settingsPrefs.getDateFlow()) {
                    it?.let {
                        this@MainViewModel.date.value = Date(it)
                    }
                }
            }
        }
    }

    fun toCalendar() {
        router.navigate(R.id.action_homeFragment_to_calendarFragment)
    }

    fun nextDay() {
        val calendar = Calendar.getInstance()
        calendar.time = settingsPrefs.getDate()?.toDate() ?: Date()
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        settingsPrefs.saveDate(calendar.time.time)
    }

    fun previousDay() {
        val calendar = Calendar.getInstance()
        calendar.time = settingsPrefs.getDate()?.toDate() ?: Date()
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        settingsPrefs.saveDate(calendar.time.time)
    }

    fun preferences() {

        router.navigate(R.id.action_global_preferences)
    }
}