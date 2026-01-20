package com.maccs.events.data.repository

import com.maccs.events.data.model.Event
import kotlinx.coroutines.delay

class FakeDataSource : EventDataSource {

    // Lista mutable para poder modificar favoritos/asistencia
    // Al ser una propiedad de la clase, vivirá mientras viva esta instancia
    private val mockEvents = mutableListOf(
        Event(
            id = "1",
            name = "Tech Fest Madrid 2024",
            date = 1710493200000L,
            time = "10:00",
            location = "Campus UC3M",
            price = 45.0,
            imageUrl = "https://images.unsplash.com/photo-1540575467063-178a50c2df87?q=80&w=1000",
            shortDescription = "El mayor evento de tecnología.",
            longDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit...",
            isFavorite = false,
            isAttending = true,
        ),
        Event(
            id = "2",
            name = "Concierto Jazz",
            date = 1711098000000L,
            time = "19:30",
            location = "Sala Barco",
            price = 12.5,
            imageUrl = "https://images.unsplash.com/photo-1514525253440-b393452e3383?q=80&w=1000",
            shortDescription = "Música suave al aire libre.",
            longDescription = "Disfruta de una velada inolvidable...",
            isFavorite = true,
            isAttending = false
        ),
        Event(
            id = "3",
            name = "Taller de Sushi",
            date = 1712307600000L,
            time = "18:00",
            location = "Don Buri",
            price = 60.0,
            imageUrl = "https://images.unsplash.com/photo-1553621042-f6e147245754?q=80&w=1000",
            shortDescription = "Aprende con el chef Kenji.",
            longDescription = "Materiales incluidos para aprender a hacer makis...",
            isFavorite = false,
            isAttending = false
        )
    )


    override suspend fun getEvents(): List<Event> {
        delay(1000) // Simulamos carga de red/DB
        return mockEvents
    }

    override suspend fun getEventById(eventId: String): Event? {
        delay(500) // Pequeña carga
        return mockEvents.find { it.id == eventId }
    }


    override suspend fun toggleFavorite(eventId: String) {
        val index = mockEvents.indexOfFirst { it.id == eventId }
        if (index != -1) {
            // Hacemos una copia del evento cambiando solo el booleano
            // (Porque en Compose/Kotlin las data class suelen ser inmutables)
            val currentEvent = mockEvents[index]
            mockEvents[index] = currentEvent.copy(isFavorite = !currentEvent.isFavorite)
        }
    }

    override suspend fun toggleAttendance(eventId: String) {
        val index = mockEvents.indexOfFirst { it.id == eventId }
        if (index != -1) {
            val currentEvent = mockEvents[index]
            mockEvents[index] = currentEvent.copy(isAttending = !currentEvent.isAttending)
        }
    }

}