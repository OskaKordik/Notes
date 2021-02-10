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
    fun saveSubOnly(show: Boolean): Boolean
    fun saveDate(time:Long): Boolean

    fun getLive(): LiveType?
    fun getScore(): Boolean?
    fun getSport(): Int?
    fun getTournament(): Int?
    fun getSubOnly(): Boolean
    fun getDate():Long?

    fun getLiveFlow(): Flow<LiveType?>
    fun getScoreFlow(): Flow<Boolean?>
    fun getSportFlow(): Flow<Int?>
    fun getTournamentFlow(): Flow<Int?>
    fun getSubOnlyFlow(): Flow<Boolean>
    fun getDateFlow(): Flow<Long?>

}

private const val LIVE = "live"
private const val SCORE = "score"
private const val SPORT = "sport"
private const val TOURNAMENT = "tournament"
private const val SUB_ONLY = "sub_only"
private const val DATE= "date"


class SettingsPrefsImpl(private val prefs: SharedPreferences): SettingsPrefs{
    override fun saveLive(type: LiveType) : Boolean = prefs.edit().putString(LIVE, type.name).commit()
    override fun saveScore(show: Boolean) : Boolean = prefs.edit().putBoolean(SCORE, show).commit()
    override fun saveSport(id: Int): Boolean  = prefs.edit().putInt(SPORT, id).commit()
    override fun saveTournament(id: Int) : Boolean = prefs.edit().putInt(TOURNAMENT, id).commit()
    override fun saveSubOnly(show: Boolean): Boolean = prefs.edit().putBoolean(SUB_ONLY, show).commit()
    override fun saveDate(time: Long): Boolean =prefs.edit().putLong(DATE,time).commit()

    override fun getLive(): LiveType? {
        val live = prefs.getString(LIVE, null)

        return if (live.isNullOrEmpty()){
            null
        }else{
            LiveType.valueOf(live)
        }
    }

    override fun getScore(): Boolean = prefs.getBoolean(SCORE,false)

    override fun getSport(): Int? {
        val sport = prefs.getInt(SPORT,-1)
        return if (sport == -1){
            null
        }else{
            sport
        }
    }

    override fun getTournament(): Int?  {
       val tour = prefs.getInt(TOURNAMENT, -1)
        return if (tour == -1){
            null
        }else{
            tour
        }
    }

    override fun getSubOnly(): Boolean = prefs.getBoolean(SUB_ONLY,false)
    override fun getDate(): Long?{
        val date = prefs.getLong(DATE,0)
        return if (date != 0L){
            date
        }else{
            return null
        }
    }

    override fun getLiveFlow(): Flow<LiveType?> = callbackFlow {
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

    override fun getSubOnlyFlow(): Flow<Boolean> = callbackFlow {
        sendBlocking(getSubOnly())
        val changeListener =
            SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                if (key == SUB_ONLY) {
                    sendBlocking(getSubOnly())
                }
            }
        prefs.registerOnSharedPreferenceChangeListener(changeListener)
        awaitClose { prefs.unregisterOnSharedPreferenceChangeListener(changeListener) }
    }

    override fun getDateFlow(): Flow<Long?> = callbackFlow {
        sendBlocking(getDate())
        val changeListener =
            SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                if (key == DATE) {
                    sendBlocking(getDate())
                }
            }
        prefs.registerOnSharedPreferenceChangeListener(changeListener)
        awaitClose { prefs.unregisterOnSharedPreferenceChangeListener(changeListener) }
    }

    override fun clear() = prefs.edit().clear().commit()

}