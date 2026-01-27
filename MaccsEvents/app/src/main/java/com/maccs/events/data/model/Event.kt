package com.maccs.events.data.model

import java.util.Date

/**
 * Modelo de datos que representa un Evento en la aplicación.
 * Usamos 'val' para que sea inmutable (buena práctica en Compose/Flow).
 * Para modificarlo, usamos el método .copy()
 */
data class  Event(
    val id: String,
    val name: String,
    val date: Long,
    val time: String,
    val price: Double,
    val location: String,
    val imageUrl: String,
    val shortDescription: String,
    val longDescription: String,

// Estados que pueden cambiar por interacción del usuario
    val isFavorite: Boolean = false,
    val isAttending: Boolean = false
)