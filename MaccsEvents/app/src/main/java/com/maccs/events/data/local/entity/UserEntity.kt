package com.maccs.events.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String, // Aquí irá el UID de Firebase
    val name: String,
    val email: String,
    val profileImagePath: String? // Ruta del archivo en la carpeta interna
)