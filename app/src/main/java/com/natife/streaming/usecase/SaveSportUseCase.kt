package com.natife.streaming.usecase

import com.natife.streaming.data.dto.sports.SportTranslateDTO
import com.natife.streaming.db.LocalSqlDataSourse
import com.natife.streaming.preferenses.SettingsPrefs
import timber.log.Timber

interface SaveSportUseCase {
    @Deprecated("don't use")
    suspend fun execute(sportId: Int)

    suspend fun execute(sport: SportTranslateDTO, isCheck: Boolean)
}

class SaveSportUseCaseImpl(
    private val settingsPrefs: SettingsPrefs,
    private val localSqlDataSourse: LocalSqlDataSourse
) : SaveSportUseCase {

    @Deprecated("don't use")
    override suspend fun execute(sportId: Int) {
        Timber.e("lkjndojoif $sportId")
        settingsPrefs.saveSport(sportId)
    }

    override suspend fun execute(sport: SportTranslateDTO, isCheck: Boolean) {
        if(isCheck) localSqlDataSourse.setPreferencesSport(sport) else  localSqlDataSourse.deletePreferencesSport(sport)
    }

}