package com.natife.streaming.usecase

import com.natife.streaming.preferenses.SettingsPrefs

interface SaveShowScoreUseCase {
    suspend fun execute(yes:Boolean)
}
class SaveShowScoreUseCaseImpl(private val settingsPrefs: SettingsPrefs): SaveShowScoreUseCase{
    override suspend fun execute(yes: Boolean) {
        settingsPrefs.saveScore(yes)
    }
}