package com.natife.streaming.usecase

import android.app.Application
import com.natife.streaming.App
import com.natife.streaming.preferenses.AuthPrefs

/**
 * Выступает в роли интерфейса между ViewModel и Api.
 * При необходимости можно реализовать свой UseCaseImpl или отредактировать имеющийся
 * в нем же можно мапить данные.
 */
interface LogoutUseCase {
    suspend fun execute()
}

class LogoutUseCaseImpl(
    private val application: Application,
    private val authPrefs: AuthPrefs
) : LogoutUseCase {
    override suspend fun execute() {
        authPrefs.clear()
        (application as? App)?.restartKoin()
    }
}
