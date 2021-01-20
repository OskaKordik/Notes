package com.natife.streaming.preferenses

import android.content.SharedPreferences
import com.natife.streaming.data.LiveType
import com.natife.streaming.data.Sport
import com.natife.streaming.data.Tournament
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber

interface SettingsPrefs: BasePrefs {
    fun saveLive(type: LiveType): Boolean
    fun saveScore(show: Boolean): Boolean
    fun saveSport(id: Int): Boolean
    fun saveTournament(id: Int): Boolean

    fun getLive(): LiveType?
    fun getScore(): Boolean?
    fun getSport(): Int?
    fun getTournament(): Int?

    fun getLiveFlow(): Flow<LiveType?>
    fun getScoreFlow(): Flow<Boolean?>
    fun getSportFlow(): Flow<Int?>
    fun getTournamentFlow(): Flow<Int?>
}

private const val LIVE = "live"
private const val SCORE = "score"
private const val SPORT = "sport"
private const val TOURNAMENT = "tournament"

class SettingsPrefsImpl(private val prefs: SharedPreferences): SettingsPrefs{
    override fun saveLive(type: LiveType) : Boolean = prefs.edit().putString(LIVE, type.name).commit()


    override fun saveScore(show: Boolean) : Boolean = prefs.edit().putBoolean(SCORE, show).commit()

    override fun saveSport(id: Int): Boolean  = prefs.edit().putInt(SPORT, id).commit()
    override fun saveTournament(id: Int) : Boolean = prefs.edit().putInt(TOURNAMENT, id).commit()

    override fun getLive(): LiveType = LiveType.valueOf(prefs.getString(LIVE, null)?:"")

    override fun getScore(): Boolean = prefs.getBoolean(SCORE,false)

    override fun getSport(): Int = prefs.getInt(SPORT,-1)

    override fun getTournament(): Int  = prefs.getInt(TOURNAMENT, -1)

    override fun getLiveFlow(): Flow<LiveType> = callbackFlow {
        sendBlocking(getLive())

        val changeListener =
            SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                if (key == LIVE) {
                    sendBlocking(getLive())
                }
            }
        prefs.registerOnSharedPreferenceChangeListener(changeListener)
        awaitClose { prefs.unregisterOnSharedPreferenceChangeListener(changeListener) }
    }

    override fun getScoreFlow(): Flow<Boolean?> = callbackFlow {
        sendBlocking(getScore())
        val changeListener =
            SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                if (key == SCORE) {
                    sendBlocking(getScore())
                }
            }
        prefs.registerOnSharedPreferenceChangeListener(changeListener)
        awaitClose { prefs.unregisterOnSharedPreferenceChangeListener(changeListener) }
    }

    override fun getSportFlow(): Flow<Int?> = callbackFlow {
        sendBlocking(getSport())

        val changeListener =
            SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                Timber.e("kkdjfdffd jljlkjlkkkkjkjjlkjlkjlkjlkj")
                if (key == SPORT) {
                    sendBlocking(getSport())
                }
            }
        prefs.registerOnSharedPreferenceChangeListener(changeListener)
        awaitClose { prefs.unregisterOnSharedPreferenceChangeListener(changeListener) }
    }

    override fun getTournamentFlow(): Flow<Int?> = callbackFlow {
        sendBlocking(getTournament())
        val changeListener =
            SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                if (key == TOURNAMENT) {
                    sendBlocking(getTournament())
                }
            }
        prefs.registerOnSharedPreferenceChangeListener(changeListener)
        awaitClose { prefs.unregisterOnSharedPreferenceChangeListener(changeListener) }
    }
    override fun clear() = prefs.edit().clear().commit()

}