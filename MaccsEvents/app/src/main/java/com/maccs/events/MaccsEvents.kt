package com.maccs.events

import android.app.Application
import androidx.room.Room
import com.maccs.events.data.local.AppDatabase
import com.maccs.events.data.repository.EventRepository


class MaccsEventsApp : Application() {

    // Instancia única de la base de datos
    private val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "maccs-events-db"
        ).build()
    }

    // Instancia única del repositorio (Accesible desde cualquier Activity)
    val container by lazy {
        EventRepository(database.eventDao())
    }
}