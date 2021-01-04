package com.natife.streaming.usecase

import android.app.Application
import android.content.Intent
import com.natife.streaming.App
import com.natife.streaming.preferenses.AuthPrefs
import com.natife.streaming.ui.splash.SplashActivity
import com.natife.streaming.utils.Result

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
        application.startActivity(Intent(application, SplashActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }
}
