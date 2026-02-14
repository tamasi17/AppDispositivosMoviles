package com.maccs.events.data.repository

import com.maccs.events.data.local.dao.EventDao
import com.maccs.events.data.local.toDomain
import com.maccs.events.data.local.toEntity
import com.maccs.events.data.model.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class EventRepository(private val eventDao: EventDao) : EventDataSource {

    // --- CONSULTAS (READ) ---

    // Obtener lista estática (una sola vez)
    override suspend fun getEvents(): List<Event> {
        return eventDao.getAllEvents().map { entities ->
            entities.map { it.toDomain() }
        }.first()
    }

    // Obtener Flow para Home (Reactivo)
    override fun getEventsFlow(): Flow<List<Event>> {
        return eventDao.getAllEvents().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    // Obtener Flow para Favoritos (Reactivo)
    override fun getFavoriteEvents(): Flow<List<Event>> {
        return eventDao.getFavoriteEvents().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    // Obtener un evento específico
    override suspend fun getEventById(eventId: String): Event? {
        return eventDao.getEventById(eventId)?.toDomain()
    }


    // --- ACCIONES (WRITE) ---

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
            val updated = it.copy(isAttending = !it.isAttending)
            eventDao.update(updated)
        }
    }

    // Usado por EventFormViewModel para crear nuevos
    suspend fun insertEvent(event: Event) {
        eventDao.insert(event.toEntity())
    }

    // Usado por EventFormViewModel para editar existentes
    suspend fun updateEvent(event: Event) {
        eventDao.update(event.toEntity())
    }

    // Usado por EventDetailViewModel para borrar
    suspend fun deleteEvent(event: Event) {
        eventDao.delete(event.toEntity())
    }
}