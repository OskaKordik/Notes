package com.natife.streaming.usecase

import com.natife.streaming.preferenses.SettingsPrefs
import timber.log.Timber

interface SaveSportUseCase {
    suspend fun execute(sportId: Int)
}

class SaveSportUseCaseImpl(private val settingsPrefs: SettingsPrefs) : SaveSportUseCase {

    override suspend fun execute(sportId: Int) {
        Timber.e("lkjndojoif $sportId")
        settingsPrefs.saveSport(sportId)
    }

}