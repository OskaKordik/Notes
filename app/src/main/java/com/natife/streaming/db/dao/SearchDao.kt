package com.natife.streaming.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.natife.streaming.db.entity.SearchEntity

@Dao
interface SearchDao {

    @Insert
    suspend fun insert(result: SearchEntity )

    @Query("SELECT * FROM search_result WHERE name LIKE :search")
    suspend fun getResults(search: String): List<SearchEntity>
}