package com.maccs.events.data.repository

import com.maccs.events.data.model.Event

interface EventDataSource {
    suspend fun getEvents(): List<Event>
    suspend fun getEventById(eventId: String): Event?
    suspend fun toggleFavorite(id: String)
    suspend fun toggleAttendance(eventId: String)
}