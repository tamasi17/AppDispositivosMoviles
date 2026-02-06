package com.maccs.events.data.model

/**
 * Modelo de datos que representa un Evento en la aplicación.
 * Usamos 'val' para que sea inmutable (buena práctica en Compose/Flow).
 * Para modificarlo, usamos el método .copy()
 */
data class  Event(
    val id: String,
    val name: String,
    val date: Long,
    val time: Long,
    val price: Double,
    val location: String,
    val imageUrl: String,
    val shortDescription: String,
    val longDescription: String,
    val isFavorite: Boolean = false,
    val isAttending: Boolean = false
)