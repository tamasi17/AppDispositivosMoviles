package com.maccs.events.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.maccs.events.data.local.entity.EventEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) providing methods to interact with the 'events' table.
 */
@Dao
interface EventDao {

    // Returns a Flow. Room monitors the tables and emits a new list automatically
    // whenever data changes. Note: Do NOT use 'suspend' here, Flow is already async.
    @Query("SELECT * FROM events")
    fun getAllEvents(): Flow<List<EventEntity>>

    // Returns a single event or null if not found.
    // Must be 'suspend' to ensure it runs off the main thread.
    @Query("SELECT * FROM events WHERE id = :id")
    suspend fun getEventById(id: String): EventEntity?

    // Inserts a list of events.
    // OnConflictStrategy.REPLACE ensures that if we pull fresh data from an API,
    // we overwrite existing local data to keep it up to date.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(events: List<EventEntity>)

    // Updates a specific event. Used primarily for toggling 'isFavorite'.
    @Update
    suspend fun update(event: EventEntity)
}