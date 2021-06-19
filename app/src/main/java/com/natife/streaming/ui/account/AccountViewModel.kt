package com.natife.streaming.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.natife.streaming.R
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.router.Router
import com.natife.streaming.usecase.AccountUseCase
import com.natife.streaming.usecase.LogoutUseCase
import com.natife.streaming.data.Profile
import com.natife.streaming.db.LocalSqlDataSourse
import com.natife.streaming.db.entity.GlobalSettings
import com.natife.streaming.db.entity.Lang

abstract class AccountViewModel : BaseViewModel() {
    abstract fun logout()
    abstract fun back()
    abstract fun setScore()
    abstract fun setLang(lang: Int)
    abstract fun toSubscriptions()
    abstract fun toPayStory()
    abstract fun initialization(lang: String)

    abstract val loadersLiveData: LiveData<Boolean>
    abstract val profileLiveData: LiveData<Profile>
    abstract val settings: LiveData<GlobalSettings>
}

class AccountViewModelImpl(
    private val router: Router,
    private val logoutUseCase: LogoutUseCase,
    private val accountUseCase: AccountUseCase,
    private val localSqlDataSourse: LocalSqlDataSourse
) : AccountViewModel() {
    override val loadersLiveData = MutableLiveData<Boolean>(true)
    override val profileLiveData = MutableLiveData<Profile>()
    override val settings = MutableLiveData<GlobalSettings>()

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
    override fun initialization(lang: String){
        launch{
            val globalSettings = localSqlDataSourse.getGlobalSettings()
            if (globalSettings == null) {
                localSqlDataSourse.setGlobalSettings(
                    showScore = false,
                    lang = Lang.valueOf(lang.uppercase())
                )
                settings.value = localSqlDataSourse.getGlobalSettings()
            } else {
                settings.value = localSqlDataSourse.getGlobalSettings()
            }
        }
    }



    override fun logout() {
        loadersLiveData.value = true
            logoutUseCase.execute(true)
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
        launch{
            localSqlDataSourse.updateGlobalSettings(
                showScore = !settings.value!!.showScore?: false,
                lang = settings?.value?.lang?: Lang.EN
            )
            val globalSettings = localSqlDataSourse.getGlobalSettings()
            if (globalSettings == null) {
                localSqlDataSourse.setGlobalSettings(
                    showScore = false,
                    lang = settings?.value?.lang?: Lang.EN
                )
                settings.value = localSqlDataSourse.getGlobalSettings()
            } else {
                settings.value = localSqlDataSourse.getGlobalSettings()
            }
        }
    }


    override fun setLang(lang: Int) {
        launch{
            localSqlDataSourse.updateGlobalSettings(
                showScore = settings.value!!.showScore?: false,
                lang = language[lang]
            )
        }
    }
}