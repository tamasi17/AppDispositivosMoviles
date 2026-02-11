package com.maccs.events.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.maccs.events.data.local.dao.EventDao
import com.maccs.events.data.local.dao.UserDao
import com.maccs.events.data.local.entity.EventEntity
import com.maccs.events.data.local.entity.UserEntity

@Database(
    entities = [EventEntity::class, UserEntity::class],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // Si la instancia no es nula, la devuelve; si lo es, crea la base de datos
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "maccs_database"
                )
                    .fallbackToDestructiveMigration() // Útil para desarrollo si cambias la versión
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}