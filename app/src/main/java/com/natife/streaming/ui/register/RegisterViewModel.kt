package com.natife.streaming.ui.register

import android.app.Application
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.natife.streaming.R
import com.natife.streaming.base.BaseViewModel
import com.natife.streaming.db.LocalSqlDataSourse
import com.natife.streaming.db.entity.Lang
import com.natife.streaming.router.Router
import com.natife.streaming.usecase.AccountUseCase
import com.natife.streaming.usecase.LoginUseCase
import com.natife.streaming.usecase.RegisterUseCase
import com.natife.streaming.utils.Result
import com.natife.streaming.workers.LoadListOfSportsWorker
import com.natife.streaming.workers.LoadListOfTournamentWorker

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
    private val application: Application
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
                                launch {
                                    accountUseCase.getProfile()
                                    loadPreferences()
                                    localSqlDataSourse.setGlobalSettings(
                                        showScore = false,
                                        lang = Lang.valueOf(lang.uppercase())
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
            OneTimeWorkRequest.Builder(LoadListOfTournamentWorker::class.java).build()
        var continuation = workManager
            .beginUniqueWork(
                "LOAD_PREFERENCES",
                ExistingWorkPolicy.REPLACE,
                loadListOfTournamentWorker
            ).then(loadListOfSportsWorker)
        continuation.enqueue()
    }
}