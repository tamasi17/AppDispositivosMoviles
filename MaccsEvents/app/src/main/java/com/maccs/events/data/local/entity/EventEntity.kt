package com.maccs.events.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.type.DateTime

/**
 * Represents the 'events' table in the SQLite database.
 * We use a Data Class to automatically generate getters, setters, and equals/hashcode.
 */
@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val date: Long, // Stored as a timestamp (Long) for efficient sorting
    val time: String, // TODO: cambiar a DateTime?
    val location: String,
    val imageUrl: String,
    val price: Double,
    val isFavorite: Boolean = false,
    val isAttending: Boolean = false,
    val shortDescription: String,
    val longDescription: String
)