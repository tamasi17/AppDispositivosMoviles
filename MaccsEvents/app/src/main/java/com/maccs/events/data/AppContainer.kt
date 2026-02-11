package com.maccs.events.data

import android.content.Context
import androidx.room.Room
import com.maccs.events.data.local.AppDatabase
import com.maccs.events.data.repository.EventRepository

// 1. La Interfaz: Define qué objetos están disponibles para la App
interface AppContainer {
    val eventRepository: EventRepository
}

// 2. La Implementación: Crea los objetos reales
class DefaultAppContainer(private val context: Context) : AppContainer {

    // Creamos la Base de Datos (Lazy = solo se crea cuando se necesita por primera vez)
    private val database: AppDatabase by lazy {
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "maccs_events_db" // Nombre del archivo de la DB
        )
            // ESTO ES CLAVE: Si cambias la versión, borra la DB vieja y crea una nueva limpia
            .fallbackToDestructiveMigration()
            .build()
    }

    // Creamos el Repositorio inyectándole el DAO de la base de datos
    override val eventRepository: EventRepository by lazy {
        EventRepository(database.eventDao())
    }
}