package com.maccs.events.ui.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.maccs.events.data.model.Event
import com.maccs.events.data.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date

data class EventFormUiState(
    val id: String = "", // Añadimos el ID al estado
    val name: String = "",
    val location: String = "",
    val date: String = "",
    val time: String = "",
    val price: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val isEditing: Boolean = false,
    val isSaved: Boolean = false
)

class EventFormViewModel(
    private val repository: EventRepository,
    private val eventId: String?
) : ViewModel() {

    private val _uiState = MutableStateFlow(EventFormUiState())
    val uiState = _uiState.asStateFlow()

    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    init {
        if (eventId != null) {
            _uiState.update { it.copy(isEditing = true, id = eventId) }
            loadEventData(eventId)
        }
    }

    private fun loadEventData(id: String) {
        viewModelScope.launch {
            val event = repository.getEventById(id)
            event?.let { e ->
                _uiState.update {
                    it.copy(
                        name = e.name,
                        location = e.location,
                        price = e.price.toString(),
                        description = e.longDescription,
                        imageUrl = e.imageUrl,
                        // Convertimos el Long de la DB a String para el TextField
                        date = dateFormatter.format(Date(e.date))
                    )
                }
            }
        }
    }

    // --- ACTUALIZACIONES DE ESTADO ---
    fun onNameChange(n: String) = _uiState.update { it.copy(name = n) }
    fun onLocationChange(l: String) = _uiState.update { it.copy(location = l) }
    fun onDateChange(d: String) = _uiState.update { it.copy(date = d) }
    fun onTimeChange(t: String) = _uiState.update { it.copy(time = t) }
    fun onPriceChange(p: String) = _uiState.update { it.copy(price = p) }
    fun onDescriptionChange(d: String) = _uiState.update { it.copy(description = d) }
    fun onImageUrlChange(url: String) = _uiState.update { it.copy(imageUrl = url) }

    fun saveEvent() {
        val s = _uiState.value

        viewModelScope.launch {
            val currentUser = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
            val currentUserId = currentUser?.uid ?: "usuario_temporal_pruebas"

            // Convertimos el String del formulario a Long para Room
            val dateAsLong = try {
                dateFormatter.parse(s.date)?.time ?: System.currentTimeMillis()
            } catch (e: Exception) {
                System.currentTimeMillis()
            }

            if (!s.isEditing) {
                // --- CREAR NUEVO ---
                val newEvent = Event(
                    id = UUID.randomUUID().toString(),
                    name = s.name,
                    location = s.location,
                    date = dateAsLong,
                    shortDescription = s.description.take(50),
                    longDescription = s.description,
                    price = s.price.toDoubleOrNull() ?: 0.0,
                    imageUrl = s.imageUrl.ifBlank { "https://picsum.photos/400/200" },
                    isFavorite = false,
                    userId = currentUserId
                )
                repository.insertEvent(newEvent)
            } else {
                // --- ACTUALIZAR ---
                val existingEvent = repository.getEventById(s.id)
                existingEvent?.let { event ->
                    val updatedEvent = event.copy(
                        name = s.name,
                        location = s.location,
                        date = dateAsLong,
                        shortDescription = s.description.take(50),
                        longDescription = s.description,
                        price = s.price.toDoubleOrNull() ?: 0.0,
                        imageUrl = s.imageUrl.ifBlank { event.imageUrl }
                    )
                    repository.updateEvent(updatedEvent)
                }
            }

            _uiState.update { it.copy(isSaved = true) }
        }
    }
}

class EventFormViewModelFactory(
    private val repository: EventRepository,
    private val eventId: String?
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventFormViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EventFormViewModel(repository, eventId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}