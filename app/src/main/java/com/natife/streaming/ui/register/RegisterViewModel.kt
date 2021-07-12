package com.natife.streaming.ui.register

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
import com.natife.streaming.usecase.RegisterUseCase
import com.natife.streaming.utils.Result
import com.natife.streaming.workers.LoadListOfSportsWorker
import com.natife.streaming.workers.LoadListOfTournamentWorkerRegistration
import java.util.*

abstract class RegisterViewModel : BaseViewModel() {
    abstract fun userRegistration(
        lang: String,
        email: String,
        password: String,
        onError: ((String?) -> Unit)
    )
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
                        loginUseCase.execute(email, password) { result ->
                            if (result.status == Result.Status.SUCCESS) {
                                val userId = result.userId
                                launch {
                                    accountUseCase.getProfile()
                                    loadPreferences()
                                    if (settingsPrefs.getDate() == null) {
                                        settingsPrefs.saveDate(
                                            Date().time
                                        )
                                    }
                                    settingsPrefs.saveLanguage(lang.uppercase()) // TODO продублировал в преференс тк не нашел решения как брать из бд при загрузке в BaseActivity
                                    localSqlDataSourse.setGlobalSettings(
                                        showScore = true,
                                        lang = Lang.valueOf(lang.uppercase()),
                                        id = userId
                                    )
                                    router.navigate(R.id.action_global_preferences)
                                }
                            } else {
                                onError.invoke(result.message)
                            }
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
            OneTimeWorkRequest.Builder(LoadListOfTournamentWorkerRegistration::class.java).build()
        val continuation = workManager
            .beginUniqueWork(
                "LOAD_PREFERENCES",
                ExistingWorkPolicy.REPLACE,
                loadListOfTournamentWorker
            ).then(loadListOfSportsWorker)
        continuation.enqueue()
    }
}