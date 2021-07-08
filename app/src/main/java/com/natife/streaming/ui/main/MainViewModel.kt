package com.natife.streaming.ui.main

import androidx.lifecycle.MutableLiveData
import com.natife.streaming.R
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.db.LocalSqlDataSourse
import com.natife.streaming.db.entity.GlobalSettings
import com.natife.streaming.db.entity.Lang
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
    private val settingsPrefs: SettingsPrefs,
    private val localSqlDataSourse: LocalSqlDataSourse,
) : BaseViewModel() {
    val date = MutableLiveData<Date>()
    val settings = MutableLiveData<GlobalSettings>()

    init {
        launch {
            val isLoggedIn = authPrefs.isLoggedIn()
            if (isLoggedIn) {
                router.navigate(R.id.action_global_nav_main)
            } else {
                router.navigate(R.id.action_global_nav_auth)
            }
        }
        setPrefToday() // при каждом заходе в приложение всегда сбиваем дату на текущюю
        date.value = Date()

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

    //if global settings are not specified, set the default
    @ExperimentalStdlibApi
    fun initialization(lang: String) =
        launch {
            val globalSettings = localSqlDataSourse.getGlobalSettings()
            if (globalSettings == null) {
                settingsPrefs.saveLanguage(lang.uppercase()) // TODO продублировал в преференс тк не нашел решения как брать из бд при загрузке в
                localSqlDataSourse.setGlobalSettings(
                    showScore = true,
                    lang = Lang.valueOf(lang.uppercase())
                )
                settings.value = localSqlDataSourse.getGlobalSettings()
            } else {
                settings.value = localSqlDataSourse.getGlobalSettings()
            }
        }

    fun getGlobalSettings() {
        launch {
            settings.value = localSqlDataSourse.getGlobalSettings()
        }
    }

    private fun setPrefToday() {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        settingsPrefs.saveDate(calendar.time.time)
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

    fun scoreButtonClicked(b: Boolean) {
        if (b) launch {
            localSqlDataSourse.updateShowScore(false)
            settings.value = localSqlDataSourse.getGlobalSettings()
        } else launch {
            localSqlDataSourse.updateShowScore(true)
            settings.value = localSqlDataSourse.getGlobalSettings()
        }

    }


}