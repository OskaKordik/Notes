package com.natife.streaming.db

import com.natife.streaming.data.Sport
import com.natife.streaming.db.dao.LocalSqlTasksDao
import com.natife.streaming.db.entity.GlobalSettings
import com.natife.streaming.db.entity.Lang
import com.natife.streaming.usecase.AccountUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalSqlDataSourse internal constructor(
    private val localSqlTasksDao: LocalSqlTasksDao,
    private val accountUseCase: AccountUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    private suspend fun getUserAuthEmail(): String =
        withContext(ioDispatcher) {
            accountUseCase.getProfile().email
        }

    private suspend fun setGlobalSettings(showScore: Boolean, lang: Lang) {
        withContext(ioDispatcher) {
            GlobalSettings(
                authEmail = getUserAuthEmail(),
                showScore = showScore,
                lang = lang
            ).let {
                localSqlTasksDao.setGlobalSettings(it)
            }
        }
    }

    //Global Settings
    suspend fun updateGlobalSettings(showScore: Boolean, lang: Lang) {
        withContext(ioDispatcher) {
            if (getGlobalSettings() == null) {
                setGlobalSettings(showScore,lang)
            } else {
                GlobalSettings(
                    authEmail = getUserAuthEmail(),
                    showScore = showScore,
                    lang = lang
                ).let {
                    localSqlTasksDao.updateGlobalSettings(it)
                }
            }
        }
    }

    suspend fun updateGlobalSettingsShowScore(showScore: Boolean) {
        withContext(ioDispatcher) {
            GlobalSettings(
                authEmail = getUserAuthEmail(),
                showScore = showScore,
                lang = getGlobalSettings().lang
            ).let {
                localSqlTasksDao.updateGlobalSettings(it)
            }
        }
    }

    suspend fun updateGlobalSettingsLang(lang: Lang) {
        withContext(ioDispatcher) {
            GlobalSettings(
                authEmail = getUserAuthEmail(),
                showScore = getGlobalSettings().showScore,
                lang = lang
            ).let {
                localSqlTasksDao.updateGlobalSettings(it)
            }
        }
    }

    suspend fun getGlobalSettings(): GlobalSettings =
        withContext(ioDispatcher) {
            localSqlTasksDao.getGlobalSettings(getUserAuthEmail())
        }

    // PreferencesSports
//    private suspend fun setPreferencesSport(sport: Sport) {
//        withContext(ioDispatcher) {
//            localSqlTasksDao.setPreferencesSport(sport)
//        }
//    }

//    suspend fun getPreferencesSport():List<Sport> =
//        withContext(ioDispatcher) {
//            localSqlTasksDao.getPreferencesSport()
//        }
//
//    suspend fun updatePreferencesSport(sport: Sport) {
//        withContext(ioDispatcher) {
//            if (getGlobalSettings() == null) {
//                setGlobalSettings(showScore,lang)
//            } else {
//                GlobalSettings(
//                    authEmail = getUserAuthEmail(),
//                    showScore = showScore,
//                    lang = lang
//                ).let {
//                    localSqlTasksDao.updateGlobalSettings(it)
//                }
//            }
//        }
//    }

}