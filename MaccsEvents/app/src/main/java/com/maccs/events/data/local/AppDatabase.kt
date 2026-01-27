package com.maccs.events.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.maccs.events.data.local.dao.EventDao
import com.maccs.events.data.local.entity.EventEntity

/**
 * The main database configuration.
 * It defines the entities included and the version number.
 */
@Database(entities = [EventEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // Exposes the DAO to the rest of the app
    abstract fun eventDao(): EventDao
}