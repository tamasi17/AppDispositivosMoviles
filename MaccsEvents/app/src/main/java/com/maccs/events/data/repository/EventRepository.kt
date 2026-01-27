package com.maccs.events.data.repository

import com.maccs.events.data.model.Event

class EventRepository {


    /**
     * Singleton (object) para simular inyección de dependencias simple.
     * Esto asegura que todos los ViewModels usen LA MISMA instancia de datos
     * (así si marcas favorito en detalle, se ve en home).
     */
    object EventRepository {

        // --- CONFIGURACIÓN DE LA FUENTE DE DATOS ---

        // AHORA (Fase 1): Usamos el Fake
        private val dataSource: EventDataSource = FakeDataSource()

        // FUTURO (Fase 2 - Room):
        // Cuando configures Room, cambiarás la línea de arriba por algo así:
        // private lateinit var dataSource: EventDataSource
        // fun init(db: AppDatabase) { dataSource = RoomDataSource(db.eventDao()) }


        // --- MÉTODOS PÚBLICOS (API) ---
        // El ViewModel solo llama a esto, no sabe qué hay detrás.

        suspend fun getEvents(): List<Event> {
            return dataSource.getEvents()
        }

        suspend fun getEventById(eventId: String): Event? {
            return dataSource.getEventById(eventId)
        }

        suspend fun toggleFavorite(eventId: String) {
            dataSource.toggleFavorite(eventId)
        }

        suspend fun toggleAttendance(eventId: String) {
            dataSource.toggleAttendance(eventId)
        }

    }
}