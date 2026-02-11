package com.maccs.events.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.maccs.events.data.local.entity.EventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    // --- CONSULTAS (Ya las tenías) ---

    @Query("SELECT * FROM events")
    fun getAllEvents(): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE id = :id")
    suspend fun getEventById(id: String): EventEntity?


    // OnConflictStrategy.REPLACE significa: "Si ya existe un evento con este ID, sobrescríbelo".
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: EventEntity)


    // Busca la fila con el mismo ID y actualiza los campos.
    @Update
    suspend fun update(event: EventEntity)

    // 3. INSERTAR LISTA (Para los datos falsos iniciales/Seed Data)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(events: List<EventEntity>)

    // 4. BORRAR
    @Delete
    suspend fun delete(event: EventEntity)

    // 5. FAVS
    @Query("SELECT * FROM events WHERE isFavorite = 1 ORDER BY date DESC")
    fun getFavoriteEvents(): Flow<List<EventEntity>>

    // Firebase Versions:

    @Query("SELECT * FROM events WHERE userId = :userId ORDER BY date DESC")
    fun getEventsForUser(userId: String): Flow<List<EventEntity>>

    // Para Favoritos (también filtrados por usuario, opcional pero recomendado)
    @Query("SELECT * FROM events WHERE userId = :userId AND isFavorite = 1 ORDER BY date DESC")
    fun getFavoriteEventsForUser(userId: String): Flow<List<EventEntity>>
}