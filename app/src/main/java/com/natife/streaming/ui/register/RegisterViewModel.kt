package com.natife.streaming.ui.register

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.gson.Gson
import com.natife.streaming.R
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.data.api.response.LoginResponse
import com.natife.streaming.data.model.Payload
import com.natife.streaming.db.LocalSqlDataSourse
import com.natife.streaming.db.entity.Lang
import com.natife.streaming.preferenses.SettingsPrefs
import com.natife.streaming.router.Router
import com.natife.streaming.usecase.AccountUseCase
import com.natife.streaming.usecase.LoginUseCase
import com.natife.streaming.usecase.RegisterUseCase
import com.natife.streaming.utils.JWTUtils
import com.natife.streaming.utils.Result
import com.natife.streaming.utils.onErrorValue
import com.natife.streaming.utils.onSuccessValue
import com.natife.streaming.workers.LoadListOfSportsWorker
import com.natife.streaming.workers.LoadListOfTournamentWorker
import com.natife.streaming.workers.LoadListOfTournamentWorkerRegistration
import java.util.*

abstract class RegisterViewModel : BaseViewModel() {
    abstract fun userRegistration(
        lang: String,
        email: String,
        password: String,
        onError: ((String?) -> Unit)
    )

    abstract val isUserLoggedIn: LiveData<Boolean>
}

class RegisterViewModelImpl(
    private val registerUseCase: RegisterUseCase,
    private val loginUseCase: LoginUseCase,
    private val accountUseCase: AccountUseCase,
    private val localSqlDataSourse: LocalSqlDataSourse,
    private val router: Router,
    private val application: Application,
    private val settingsPrefs: SettingsPrefs,
) : RegisterViewModel() {
    private val workManager = WorkManager.getInstance(application)
    override val isUserLoggedIn = MutableLiveData<Boolean>()
    @ExperimentalStdlibApi
    override fun userRegistration(
        lang: String,
        email: String,
        password: String,
        onError: (String?) -> Unit
    ) {
        launch {
            registerUseCase.execute(email, password) { result ->
                if (result.status == Result.Status.SUCCESS) {
                    launch {
                        loginUseCase.execute(email, password)
                            .onSuccessValue { loginResponse ->
                                if (loginResponse.error == null) {
                                    setGlobalSettings(loginResponse)
                                } else {
                                    onError.invoke(loginResponse.error)
                                }
                            }
                            .onErrorValue {
                                onError.invoke(it.message)
                            }
                    }
                } else {
                    onError.invoke(result.message)
                }
            }
        }
    }

    private fun setGlobalSettings(loginResponse: LoginResponse) {
        launch {
            accountUseCase.getProfile()
            //проверка всех настроек если клиент вошел под старым аккаунтом на новую установку
            if (localSqlDataSourse.getGlobalSettings() == null) {
                settingsPrefs.saveLanguage(application.resources.getString(R.string.lang)) // TODO продублировал в преференс тк не нашел решения как брать из бд при загрузке в BaseActivity
                localSqlDataSourse.setGlobalSettings(
                    showScore = true,
                    lang = Lang.valueOf(application.resources.getString(R.string.lang)),
                    id = getUserId(loginResponse)
                )
            }
            setDate()
            loadPreferences()
            router.navigate(R.id.action_global_nav_main)
        }
    }

    private fun getUserId(loginResponse: LoginResponse): Int? {
        val jsonBody = JWTUtils.decoded(loginResponse.accessToken ?: "")
        val payload = Gson().fromJson(jsonBody, Payload::class.java)
        return payload.sub
    }

    private fun setDate() {
        if (settingsPrefs.getDate() == null) {
            settingsPrefs.saveDate(Date().time)
        }
    }

    private fun loadPreferences() {
        val loadListOfSportsWorker =
            OneTimeWorkRequest.Builder(LoadListOfSportsWorker::class.java).build()
        val loadListOfTournamentWorker =
            OneTimeWorkRequest.Builder(LoadListOfTournamentWorker::class.java).build()
        val continuation = workManager
            .beginUniqueWork(
                "LOAD_PREFERENCES",
                ExistingWorkPolicy.REPLACE,
                loadListOfTournamentWorker
            ).then(loadListOfSportsWorker)
        continuation.enqueue()
    }
}