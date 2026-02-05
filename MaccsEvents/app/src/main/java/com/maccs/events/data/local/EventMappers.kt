package com.maccs.events.data.local

import com.maccs.events.data.local.entity.EventEntity
import com.maccs.events.data.model.Event // Asegúrate de importar tu modelo de UI

// Convierte de la Base de Datos -> UI
fun EventEntity.toDomain(): Event {
    return Event(
        id = this.id,
        name = this.name,
        date = this.date, // Si tu UI usa String, aquí tendrás que formatearlo
        location = this.location,
        imageUrl = this.imageUrl,
        price = this.price,
        isFavorite = this.isFavorite,
        time = this.time,
        shortDescription = this.shortDescription,
        longDescription = this.longDescription,
        isAttending = this.isAttending
    )
}

// Convierte de la UI -> Base de Datos (Para guardar/actualizar)
fun Event.toEntity(): EventEntity {
    return EventEntity(
        id = this.id,
        name = this.name,
        date = this.date,
        location = this.location,
        imageUrl = this.imageUrl,
        price = this.price,
        isFavorite = this.isFavorite
    )
}