package com.natife.streaming.db

import com.natife.streaming.data.dto.sports.SportTranslateDTO
import com.natife.streaming.data.dto.tournament.TournamentTranslateDTO
import com.natife.streaming.db.dao.LocalSqlTasksDao
import com.natife.streaming.db.entity.*
import com.natife.streaming.preferenses.AuthPrefs
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class LocalSqlDataSourse internal constructor(
    private val localSqlTasksDao: LocalSqlTasksDao,
    private val authPrefs: AuthPrefs,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    private suspend fun getUserAuthEmail(): String? =
        withContext(ioDispatcher) {
            authPrefs.getProfile()?.email
        }

    //Global Settings
    suspend fun setGlobalSettings(showScore: Boolean, lang: Lang) {
        withContext(ioDispatcher) {
            getUserAuthEmail()?.let { userAuthEmail ->
                GlobalSettings(
                    authEmail = userAuthEmail,
                    showScore = showScore,
                    lang = lang
                ).let {
                    localSqlTasksDao.setGlobalSettings(it)
                }
            }
        }
    }

    suspend fun updateGlobalSettings(showScore: Boolean, lang: Lang) {
        withContext(ioDispatcher) {
            if (getGlobalSettings() == null) {
                setGlobalSettings(showScore, lang)
            } else {
                getUserAuthEmail()?.let { userAuthEmail ->
                    GlobalSettings(
                        authEmail = userAuthEmail,
                        showScore = showScore,
                        lang = lang
                    ).let {
                        localSqlTasksDao.updateGlobalSettings(it)
                    }
                }
            }
        }
    }

    suspend fun updateGlobalSettingsShowScore(showScore: Boolean) {
        withContext(ioDispatcher) {
            getUserAuthEmail()?.let { userAuthEmail ->
                getGlobalSettings()?.let { globalSettings ->
                    GlobalSettings(
                        authEmail = userAuthEmail,
                        showScore = showScore,
                        lang = globalSettings.lang
                    ).let {
                        localSqlTasksDao.updateGlobalSettings(it)
                    }
                }
            }
        }
    }

    suspend fun updateGlobalSettingsLang(lang: Lang) {
        withContext(ioDispatcher) {
            getUserAuthEmail()?.let { userAuthEmail ->
                getGlobalSettings()?.let { globalSettings ->
                    GlobalSettings(
                        authEmail = userAuthEmail,
                        showScore = globalSettings.showScore,
                        lang = lang
                    ).let {
                        localSqlTasksDao.updateGlobalSettings(it)
                    }
                }
            }
        }
    }

    suspend fun getGlobalSettings(): GlobalSettings? =
        withContext(ioDispatcher) {
            getUserAuthEmail()?.let { userAuthEmail ->
                localSqlTasksDao.getGlobalSettings(userAuthEmail)
            }
        }

    // PreferencesSports
    suspend fun setPreferencesSport(sport: SportTranslateDTO) {
        withContext(ioDispatcher) {
            localSqlTasksDao.setPreferencesSport(sport.toPreferencesSport())
        }
    }

    fun getPreferencesSport(): Flow<List<PreferencesSport>> =
            localSqlTasksDao.getPreferencesSport()


    suspend fun updatePreferencesSport(sport: SportTranslateDTO) {
        withContext(ioDispatcher) {
                localSqlTasksDao.updatePreferencesSport(sport.toPreferencesSport())
        }
    }

    suspend fun deletePreferencesSport(sport: SportTranslateDTO) {
        withContext(ioDispatcher) {
            localSqlTasksDao.deletePreferencesSport(sport.toPreferencesSport())
        }
    }

    //PreferencesTournament
    suspend fun setPreferencesTournament(tournament: TournamentTranslateDTO) {
        withContext(ioDispatcher) {
            localSqlTasksDao.setPreferencesTournament(tournament.toPreferencesTournament())
        }
    }

    fun getPreferencesTournament():Flow<List<PreferencesTournament>> =
            localSqlTasksDao.getPreferencesTournament()

    suspend fun updatePreferencesTournament(tournament: TournamentTranslateDTO) {
        withContext(ioDispatcher) {
                localSqlTasksDao.updatePreferencesTournament(tournament.toPreferencesTournament())
            }
    }

    suspend fun deletePreferencesTournament(tournament: TournamentTranslateDTO) {
        withContext(ioDispatcher) {
            localSqlTasksDao.deletePreferencesTournament(tournament.toPreferencesTournament())
        }
    }
}






