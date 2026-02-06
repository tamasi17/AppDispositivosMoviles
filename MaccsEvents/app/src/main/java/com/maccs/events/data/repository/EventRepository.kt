package com.maccs.events.data.repository

import com.maccs.events.data.local.dao.EventDao
import com.maccs.events.data.local.toDomain
import com.maccs.events.data.local.toEntity
import com.maccs.events.data.model.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class EventRepository(private val eventDao: EventDao) {

//    // 1. Obtener eventos (La magia del Flow: se actualiza solo si cambia la DB)
//    val events: Flow<List<Event>> = eventDao.getAllEvents().map { entities ->
//        entities.map { it.toDomain() } // Usamos el mapper aquí
//    }

    fun getEvents(): Flow<List<Event>> {
        return eventDao.getAllEvents().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    // 2. Obtener un evento por ID
    suspend fun getEventById(id: String): Event? {
        return eventDao.getEventById(id)?.toDomain()
    }

    // 3. Toggle Favorito
    suspend fun toggleFavorite(event: Event) {
        val updatedEvent = event.copy(isFavorite = !event.isFavorite)
        eventDao.update(updatedEvent.toEntity())
    }

    // In EventRepository.kt
    fun getFavoriteEvents(): Flow<List<Event>> {
        return eventDao.getFavoriteEvents().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun insertEvent(event: Event) {
        eventDao.insert(event.toEntity()) // Asumiendo que tu DAO tiene @Insert
    }

    suspend fun updateEvent(event: Event) {
        eventDao.update(event.toEntity()) // Asumiendo que tu DAO tiene @Update
    }
}