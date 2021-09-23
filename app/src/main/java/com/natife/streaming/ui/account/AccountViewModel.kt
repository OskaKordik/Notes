package com.natife.streaming.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.natife.streaming.R
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.data.Profile
import com.natife.streaming.db.LocalSqlDataSourse
import com.natife.streaming.db.entity.GlobalSettings
import com.natife.streaming.db.entity.Lang
import com.natife.streaming.ext.Event
import com.natife.streaming.preferenses.SettingsPrefs
import com.natife.streaming.router.Router
import com.natife.streaming.usecase.AccountUseCase
import com.natife.streaming.usecase.LogoutUseCase
import com.natife.streaming.utils.TokenRefreshLoop
import com.natife.streaming.utils.VideoHeaderUpdater

abstract class AccountViewModel : BaseViewModel() {
    abstract fun logout()
    abstract fun back()
    abstract fun setScore()
    abstract fun setLang(lang: Int)
    abstract fun toSubscriptions()
    abstract fun toPayStory()
    abstract fun initialization(lang: String)
    abstract fun openLanguageSelectionDialog()

    abstract val loadersLiveData: LiveData<Boolean>
    abstract val profileLiveData: LiveData<Profile>
    abstract val settings: LiveData<GlobalSettings>
    abstract var lastposition: Int?
    abstract val restart: LiveData<Event<Boolean>>

}

class AccountViewModelImpl(
    private val router: Router,
    private val logoutUseCase: LogoutUseCase,
    private val accountUseCase: AccountUseCase,
    private val localSqlDataSourse: LocalSqlDataSourse,
    private val settingsPrefs: SettingsPrefs,
    private val tokenRefreshLoop: TokenRefreshLoop,
    private val videoHeaderUpdater: VideoHeaderUpdater
) : AccountViewModel() {
    override val loadersLiveData = MutableLiveData<Boolean>(true)
    override val restart = MutableLiveData<Event<Boolean>>()
    override val profileLiveData = MutableLiveData<Profile>()
    override val settings = MutableLiveData<GlobalSettings>()
    override var lastposition: Int? = null

    val language = arrayOf(Lang.EN, Lang.RU)

    init {
        launch {
            loadersLiveData.value = true
            val profile = accountUseCase.getProfile()
            profileLiveData.value = profile
            loadersLiveData.value = false
        }
    }

    @ExperimentalStdlibApi
    override fun initialization(lang: String) {
        launch {
            val globalSettings = localSqlDataSourse.getGlobalSettings()
            if (globalSettings == null) {
                settingsPrefs.saveLanguage(lang.uppercase()) // TODO продублировал в преференс тк не нашел решения как брать из бд при загрузке в BaseActivity
                localSqlDataSourse.setGlobalSettingsCurrentUser(
                    showScore = false,
                    lang = Lang.valueOf(lang.uppercase()),
                )
                settings.value = localSqlDataSourse.getGlobalSettings()
            } else {
                settings.value = localSqlDataSourse.getGlobalSettings()
            }
        }
    }


    override fun logout() {
        loadersLiveData.value = true
        tokenRefreshLoop.stop()
        videoHeaderUpdater.stop()
        logoutUseCase.execute(false)
        loadersLiveData.value = false
    }

    override fun back() {
        router.navigateUp()
    }

    override fun toSubscriptions() {
        router.navigate(R.id.action_accountFragment_to_subscriptionFragment)
    }

    override fun toPayStory() {
        router.navigate(R.id.action_accountFragment_to_payStoryFragment)
    }

    override fun setScore() {
        launch {
            localSqlDataSourse.updateGlobalSettingsCurrentUser(
                showScore = !settings.value!!.showScore ?: false,
                lang = settings?.value?.lang ?: Lang.EN
            )
            val globalSettings = localSqlDataSourse.getGlobalSettings()
            if (globalSettings == null) {
                localSqlDataSourse.setGlobalSettingsCurrentUser(
                    showScore = true,
                    lang = settings?.value?.lang ?: Lang.EN,
                )
                settings.value = localSqlDataSourse.getGlobalSettings()
            } else {
                settings.value = localSqlDataSourse.getGlobalSettings()
            }
        }
    }

    override fun setLang(lang: Int) {
        if ((lastposition != null) && (lastposition != lang)) launch {
            settingsPrefs.saveLanguage(language[lang].name) // TODO продублировал в преференс тк не нашел решения как брать из бд при загрузке в BaseActivity
            localSqlDataSourse.updateGlobalSettingsCurrentUser(
                showScore = settings.value!!.showScore ?: false,
                lang = language[lang]
            )
            restart.value = Event(true)
        }
    }

    override fun openLanguageSelectionDialog() {
        TODO("Not yet implemented")
    }
}