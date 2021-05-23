package com.natife.streaming.db.dao

import androidx.room.*
import com.natife.streaming.data.Sport
import com.natife.streaming.db.entity.GlobalSettings

@Dao
interface LocalSqlTasksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setGlobalSettings(globalSettings: GlobalSettings)

    @Update
    fun updateGlobalSettings(globalSettings: GlobalSettings)

    @Query ("SELECT * FROM 'GlobalSettings' WHERE authEmail ==:authEmail")
    fun getGlobalSettings(authEmail: String): GlobalSettings

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun setPreferencesSport(sport: Sport)
}