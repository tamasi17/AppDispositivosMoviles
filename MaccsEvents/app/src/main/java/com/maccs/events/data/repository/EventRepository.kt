package com.maccs.events.data.repository

import com.maccs.events.data.local.dao.EventDao
import com.maccs.events.data.local.toDomain
import com.maccs.events.data.local.toEntity
import com.maccs.events.data.model.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

// Hacemos que el repositorio implemente la interfaz
class EventRepository(private val eventDao: EventDao) : EventDataSource {

    // Cambiamos a Flow para que la UI se actualice en tiempo real
    override suspend fun getEvents(): List<Event> {
        // .first() toma el primer valor emitido por el Flow del DAO
        return eventDao.getAllEvents().map { entities ->
            entities.map { it.toDomain() }
        }.first()
    }

    override suspend fun getEventById(eventId: String): Event? {
        return eventDao.getEventById(eventId)?.toDomain()
    }

    override suspend fun toggleFavorite(id: String) {
        val entity = eventDao.getEventById(id)
        entity?.let {
            val updated = it.copy(isFavorite = !it.isFavorite)
            eventDao.update(updated)
        }
    }

    override suspend fun toggleAttendance(eventId: String) {
        val entity = eventDao.getEventById(eventId)
        entity?.let {
            val updated = it.copy(isAttending = !it.isAttending) // Asegúrate que isAttending exista en tu Entity
            eventDao.update(updated)
        }
    }

    // --- Métodos extra que no están en la interfaz pero son útiles ---

    override fun getEventsFlow(): Flow<List<Event>> {
        return eventDao.getAllEvents().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getFavoriteEvents(): Flow<List<Event>> {
        return eventDao.getFavoriteEvents().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun insertEvent(event: Event) {
        eventDao.insert(event.toEntity())
    }
}