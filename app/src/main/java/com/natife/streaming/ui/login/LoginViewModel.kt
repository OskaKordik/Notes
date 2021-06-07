package com.natife.streaming.ui.login

import android.app.Application
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.natife.streaming.R
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.db.LocalSqlDataSourse
import com.natife.streaming.db.entity.Lang
import com.natife.streaming.preferenses.SettingsPrefs
import com.natife.streaming.router.Router
import com.natife.streaming.usecase.AccountUseCase
import com.natife.streaming.usecase.LoginUseCase
import com.natife.streaming.utils.Result
import com.natife.streaming.workers.LoadListOfSportsWorker
import com.natife.streaming.workers.LoadListOfTournamentWorker
import com.natife.streaming.workers.UpdateListOfSportsWorker
import com.natife.streaming.workers.UpdateListOfTournamentWorker
import java.util.*

abstract class LoginViewModel : BaseViewModel() {

    abstract fun login(email: String, password: String, onError: ((String?) -> Unit))
    abstract fun onRegisterClicked()
}

class LoginViewModelImpl(
    private val router: Router,
    private val loginUseCase: LoginUseCase,
    private val accountUseCase: AccountUseCase,
    private val localSqlDataSourse: LocalSqlDataSourse,
    private val application: Application,
    private val settingsPrefs: SettingsPrefs,
) : LoginViewModel() {
    private val workManager = WorkManager.getInstance(application)
    override fun login(email: String, password: String, onError: ((String?) -> Unit)) {
        launch {
            loginUseCase.execute(email, password) { result ->
                if (result.status == Result.Status.SUCCESS) {
                    launch {
                        accountUseCase.getProfile()
                        //проверка всех настроек если клиент вошел под старым аккаунтом на новую установку
                        if (localSqlDataSourse.getGlobalSettings() == null) {
                            localSqlDataSourse.setGlobalSettings(
                                showScore = false,
                                lang = Lang.valueOf(application.resources.getString(R.string.lang))
                            )
                        }
                        if (settingsPrefs.getDate() == null) {
                            settingsPrefs.saveDate(Date().time)
                        }
                        if (localSqlDataSourse.getPreferencesSport().isEmpty()
                            || localSqlDataSourse.getPreferencesTournament().isEmpty()
                        ) {
                            loadPreferences()
                            router.navigate(R.id.action_global_preferences)
                        } else {
                            // если все настройки в наличии
                            updatePreferences()
                            router.navigate(R.id.action_global_nav_main)
                        }
                    }
                } else {
                    onError.invoke(result.message)
                }
            }
        }
    }

    private fun loadPreferences() {
        val loadListOfSportsWorker =
            OneTimeWorkRequest.Builder(LoadListOfSportsWorker::class.java).build()
        val loadListOfTournamentWorker =
            OneTimeWorkRequest.Builder(LoadListOfTournamentWorker::class.java).build()
        var continuation = workManager
            .beginUniqueWork(
                "LOAD_PREFERENCES",
                ExistingWorkPolicy.REPLACE,
                loadListOfTournamentWorker
            ).then(loadListOfSportsWorker)
        continuation.enqueue()
    }

    private fun updatePreferences() {
        val updateListOfSportsWorker =
            OneTimeWorkRequest.Builder(UpdateListOfSportsWorker::class.java).build()
        val updateListOfTournamentWorker =
            OneTimeWorkRequest.Builder(UpdateListOfTournamentWorker::class.java).build()
        var continuation = workManager
            .beginUniqueWork(
                "UPDATE_PREFERENCES",
                ExistingWorkPolicy.REPLACE,
                updateListOfTournamentWorker
            ).then(updateListOfSportsWorker)
        continuation.enqueue()
    }

    override fun onRegisterClicked() {
        val navDirections = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
        router.navigate(navDirections)
    }
}

