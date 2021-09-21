package com.natife.streaming.ui.account.language

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.db.LocalSqlDataSourse
import com.natife.streaming.db.entity.Lang
import com.natife.streaming.db.entity.LanguageModel
import com.natife.streaming.ext.Event
import com.natife.streaming.preferenses.SettingsPrefs

abstract class LanguageSelectionViewModel : BaseViewModel() {
    abstract fun setLang(lm: LanguageModel)

    abstract var languages: List<LanguageModel>
    abstract val restart: LiveData<Event<Boolean>>
}

class LanguageSelectionViewModelImpl(
    private val localSqlDataSourse: LocalSqlDataSourse,
    private val settingsPrefs: SettingsPrefs
) : LanguageSelectionViewModel() {

    override var languages =
        listOf(
            LanguageModel(0, "Русский", Lang.RU, getCurrentLang() == Lang.RU.name),
            LanguageModel(1, "English", Lang.EN, getCurrentLang() == Lang.EN.name)
        )

    override val restart = MutableLiveData<Event<Boolean>>()

    private fun getCurrentLang(): String = settingsPrefs.getLanguage()

    //TODO work with it when data on the BE is ready
    override fun setLang(lm: LanguageModel) {
        if (lm.isCurrent) {
            restart.value = Event(false)
        } else {
            launch {
                settingsPrefs.saveLanguage(lm.lang.name)
                localSqlDataSourse.updateGlobalSettingsCurrentUser(
                    showScore = false,
                    lang = lm.lang
                )
                restart.value = Event(true)
            }
        }
    }
}