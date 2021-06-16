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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setPreferencesSportList(sport: List<PreferencesSport>)

    @Query("SELECT * FROM 'PreferencesSport'")
    fun getPreferencesSportFlow(): Flow<List<PreferencesSport>>

    @Query("SELECT * FROM 'PreferencesSport'")
    fun getPreferencesSport(): List<PreferencesSport>

    @Query("SELECT * FROM 'PreferencesSport' WHERE id ==:id")
    fun getPreferencesSportByID(id: Int): PreferencesSport?

    @Update
    fun updatePreferencesSport(sport: PreferencesSport)

    @Delete
    fun deletePreferencesSport(sport: PreferencesSport)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setListPreferencesTournament(preferencesTournament: List<PreferencesTournament>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun updateTournamentList(preferencesTournament: List<PreferencesTournament>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setPreferencesTournament(preferencesTournament: PreferencesTournament)

    @Query("SELECT * FROM 'PreferencesTournament'")
    fun getPreferencesTournamentFlow(): Flow<List<PreferencesTournament>>

    @Query("SELECT * FROM 'PreferencesTournament'")
    fun getPreferencesTournament(): List<PreferencesTournament>


    @Query("SELECT * FROM 'PreferencesTournament' WHERE nameRus LIKE :queryString OR countryNameRus LIKE :queryString")
    fun searchRUPreferencesTournament(queryString: String): List<PreferencesTournament>

    @Query("SELECT * FROM 'PreferencesTournament' WHERE nameEng LIKE :queryString OR countryNameEng LIKE :queryString")
    fun searchENPreferencesTournament(queryString: String): List<PreferencesTournament>

    @Query("SELECT * FROM 'PreferencesTournament' WHERE (nameRus LIKE :queryString OR countryNameRus LIKE :queryString) AND sport == :id")
    fun searchRUPreferencesTournamentSportID(
        queryString: String,
        id: Int
    ): List<PreferencesTournament>

    @Query("SELECT * FROM 'PreferencesTournament' WHERE (nameEng LIKE :queryString OR countryNameEng LIKE :queryString) AND sport == :id")
    fun searchENPreferencesTournamentSportID(
        queryString: String,
        id: Int
    ): List<PreferencesTournament>


    @Query("SELECT * FROM 'PreferencesTournament' WHERE nameRus LIKE :queryString OR countryNameRus LIKE :queryString")
    fun searchRUPreferencesTournamentFlow(queryString: String): Flow<List<PreferencesTournament>>

    @Query("SELECT * FROM 'PreferencesTournament' WHERE nameEng LIKE :queryString OR countryNameEng LIKE :queryString")
    fun searchENPreferencesTournamentFlow(queryString: String): Flow<List<PreferencesTournament>>

    @Query("SELECT * FROM 'PreferencesTournament' WHERE (nameRus LIKE :queryString OR countryNameRus LIKE :queryString) AND sport == :id")
    fun searchRUPreferencesTournamentSportIDFlow(
        queryString: String,
        id: Int
    ): Flow<List<PreferencesTournament>>

    @Query("SELECT * FROM 'PreferencesTournament' WHERE (nameEng LIKE :queryString OR countryNameEng LIKE :queryString) AND sport == :id")
    fun searchENPreferencesTournamentSportIDFlow(
        queryString: String,
        id: Int
    ): Flow<List<PreferencesTournament>>


    @Query("SELECT * FROM 'PreferencesTournament' WHERE id ==:id AND sport ==:sport AND tournamentType ==:tournamentType")
    fun getPreferencesTournamentByID(
        id: Int,
        sport: Int,
        tournamentType: Int
    ): PreferencesTournament

    @Query("SELECT * FROM 'PreferencesTournament' WHERE sport ==:sportId")
    fun getPreferencesTournamentBySport(sportId: Int): List<PreferencesTournament>

    @Query("SELECT * FROM 'PreferencesTournament' WHERE sport ==:sportId AND id ==:prefID ")
    fun getPreferencesTournamentBySportIDPrefID(sportId: Int, prefID: Int): PreferencesTournament?

    @Update
    fun updatePreferencesTournament(preferencesTournament: PreferencesTournament)

    @Delete
    fun deletePreferencesTournament(preferencesTournament: PreferencesTournament)

    @Update
    fun updatePreferencesTournamentList(tournament: List<PreferencesTournament>)


}