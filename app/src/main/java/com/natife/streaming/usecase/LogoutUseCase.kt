package com.natife.streaming.usecase

import android.app.Application
import com.natife.streaming.App
import com.natife.streaming.preferenses.AuthPrefs
import com.natife.streaming.router.Router

/**
 * Выступает в роли интерфейса между ViewModel и Api.
 * При необходимости можно реализовать свой UseCaseImpl или отредактировать имеющийся
 * в нем же можно мапить данные.
 */
interface LogoutUseCase {
    fun execute()
}

class LogoutUseCaseImpl(
    private val application: Application,
    private val authPrefs: AuthPrefs,
    private val router: Router
) : LogoutUseCase {
    override fun execute() {
        authPrefs.clear()
        (application as? App)?.restartKoin()
        router.toLogin()
    }
}
