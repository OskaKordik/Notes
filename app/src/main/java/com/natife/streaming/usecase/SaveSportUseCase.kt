package com.natife.streaming.usecase

import com.natife.streaming.preferenses.SettingsPrefs

interface SaveSportUseCase {
    suspend fun execute(sportId: Int)
}

class SaveSportUseCaseImpl(private val settingsPrefs: SettingsPrefs) : SaveSportUseCase {

    override suspend fun execute(sportId: Int) {
        settingsPrefs.saveSport(sportId)
    }

}