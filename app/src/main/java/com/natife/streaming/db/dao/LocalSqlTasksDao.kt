package com.natife.streaming.db.dao

import androidx.room.*
import com.natife.streaming.db.entity.GlobalSettings
import com.natife.streaming.db.entity.PreferencesSport
import com.natife.streaming.db.entity.PreferencesTournament
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalSqlTasksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setGlobalSettings(globalSettings: GlobalSettings)

    @Update
    fun updateGlobalSettings(globalSettings: GlobalSettings)

    @Query("SELECT * FROM 'GlobalSettings' WHERE authEmail ==:authEmail")
    fun getGlobalSettings(authEmail: String): GlobalSettings

    @Query("SELECT * FROM 'GlobalSettings' WHERE authEmail ==:authEmail")
    fun getGlobalSettingsFlow(authEmail: String): Flow<GlobalSettings>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setPreferencesSport(sport: PreferencesSport)

    @Query("SELECT * FROM 'PreferencesSport'")
    fun getPreferencesSport(): Flow<List<PreferencesSport>>

    @Update
    fun updatePreferencesSport(sport: PreferencesSport)

    @Delete
    fun deletePreferencesSport(sport: PreferencesSport)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setPreferencesTournament(preferencesTournament: PreferencesTournament)

    @Query ("SELECT * FROM 'PreferencesTournament'")
    fun getPreferencesTournament(): Flow<List<PreferencesTournament>>

    @Update
    fun updatePreferencesTournament(preferencesTournament: PreferencesTournament)

    @Delete
    fun deletePreferencesTournament(preferencesTournament: PreferencesTournament)
}