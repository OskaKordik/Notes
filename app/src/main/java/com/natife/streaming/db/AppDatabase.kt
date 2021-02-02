package com.natife.streaming.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.natife.streaming.db.dao.ActionDao
import com.natife.streaming.db.entity.ActionEntity

@Database(
    entities = [ActionEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun actionDao(): ActionDao
}