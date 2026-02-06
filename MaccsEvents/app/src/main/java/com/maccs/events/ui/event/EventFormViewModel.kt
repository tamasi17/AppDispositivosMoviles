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

// Estado del formulario
data class EventFormUiState(
    val name: String = "",
    val location: String = "",
    val price: String = "",
    val description: String = "",
    val imageUrl: String = "", // URL por defecto o vacía
    val isSaved: Boolean = false, // Para saber cuándo cerrar la pantalla
    val isValid: Boolean = false
)

class EventFormViewModel(
    private val repository: EventRepository,
    private val eventId: String? // Si es null = Crear nuevo. Si tiene valor = Editar.
) : ViewModel() {

    private val _uiState = MutableStateFlow(EventFormUiState())
    val uiState = _uiState.asStateFlow()

    init {
        if (eventId != null) {
            // MODO EDICIÓN: Cargar datos existentes
            viewModelScope.launch {
                val event = repository.getEventById(eventId)
                if (event != null) {
                    _uiState.update {
                        it.copy(
                            name = event.name,
                            location = event.location,
                            price = event.price.toString(),
                            description = event.longDescription, // O shortDescription
                            imageUrl = event.imageUrl
                        )
                    }
                }
            }
        }
    }

    fun onNameChange(newValue: String) { _uiState.update { it.copy(name = newValue) } }
    fun onLocationChange(newValue: String) { _uiState.update { it.copy(location = newValue) } }
    fun onPriceChange(newValue: String) { _uiState.update { it.copy(price = newValue) } }
    fun onDescriptionChange(newValue: String) { _uiState.update { it.copy(description = newValue) } }

    fun saveEvent() {
        val currentState = _uiState.value

        // Validación básica
        if (currentState.name.isBlank() || currentState.location.isBlank()) return

        viewModelScope.launch {
            if (eventId == null) {
                // --- CREAR NUEVO ---
                val newEvent = Event(
                    id = UUID.randomUUID().toString(), // Generar ID único
                    name = currentState.name,
                    location = currentState.location,
                    price = currentState.price.toDoubleOrNull() ?: 0.0,
                    imageUrl = currentState.imageUrl.ifBlank { "https://picsum.photos/seed/${System.currentTimeMillis()}/400/200" }, // Imagen random si no hay
                    date = System.currentTimeMillis(), // Fecha actual por defecto
                    time = System.currentTimeMillis(),
                    shortDescription = currentState.description.take(50),
                    longDescription = currentState.description,
                    isFavorite = false
                )
                repository.insertEvent(newEvent)
            } else {
                // --- ACTUALIZAR EXISTENTE ---
                // Primero recuperamos el original para no perder datos como la fecha o favoritos
                val originalEvent = repository.getEventById(eventId)
                if (originalEvent != null) {
                    val updatedEvent = originalEvent.copy(
                        name = currentState.name,
                        location = currentState.location,
                        price = currentState.price.toDoubleOrNull() ?: 0.0,
                        longDescription = currentState.description
                    )
                    repository.updateEvent(updatedEvent)
                }
            }
            // Avisar a la UI que ya hemos guardado
            _uiState.update { it.copy(isSaved = true) }
        }
    }
}

// Factory para inyectar Repository y ID
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