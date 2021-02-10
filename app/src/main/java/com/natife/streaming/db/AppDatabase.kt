package com.natife.streaming.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.natife.streaming.db.dao.ActionDao
import com.natife.streaming.db.dao.SearchDao
import com.natife.streaming.db.entity.ActionEntity
import com.natife.streaming.db.entity.SearchEntity

@Database(
    entities = [ActionEntity::class,
        SearchEntity::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun actionDao(): ActionDao
    abstract fun searchDao(): SearchDao
}