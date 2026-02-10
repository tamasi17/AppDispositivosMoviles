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

data class EventFormUiState(
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

    init {
        _uiState.update { it.copy(isEditing = eventId != null) }
        if (eventId != null) {
            viewModelScope.launch {
                val event = repository.getEventById(eventId)
                event?.let { e ->
                    _uiState.update {
                        it.copy(
                            name = e.name,
                            location = e.location,
                            price = e.price.toString(),
                            description = e.longDescription,
                            imageUrl = e.imageUrl,
                            date = "01/01/2026" // Valor por defecto o extraído de e.date
                        )
                    }
                }
            }
        }
    }

    fun onNameChange(n: String) = _uiState.update { it.copy(name = n) }
    fun onLocationChange(l: String) = _uiState.update { it.copy(location = l) }
    fun onDateChange(d: String) = _uiState.update { it.copy(date = d) }
    fun onTimeChange(t: String) = _uiState.update { it.copy(time = t) }
    fun onPriceChange(p: String) = _uiState.update { it.copy(price = p) }
    fun onDescriptionChange(d: String) = _uiState.update { it.copy(description = d) }
    fun onImageUrlChange(url: String) {
        _uiState.update { it.copy(imageUrl = url) }
    }
    fun saveEvent() {
        val s = _uiState.value

        viewModelScope.launch {
            // 1. Obtener el Usuario (Lógica de Firebase)
            // Usamos un ID temporal si el login aún no está listo
            val currentUser = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
            val currentUserId = currentUser?.uid ?: "usuario_temporal_pruebas"

            if (eventId == null) {
                // --- CASO 1: CREAR NUEVO EVENTO ---
                val newEvent = Event(
                    id = UUID.randomUUID().toString(),
                    name = s.name,
                    location = s.location,
                    date = s.date, // Asumiendo que s.date tiene el valor del DatePicker
                    description = s.description,
                    price = s.price.toDoubleOrNull() ?: 0.0,
                    // Si no hay foto, ponemos una aleatoria de Lorem Picsum
                    imageUrl = s.imageUrl.ifBlank { "https://picsum.photos/400/200" },
                    isFavorite = false,
                    userId = currentUserId // <--- ASIGNAMOS EL DUEÑO AQUÍ
                )
                repository.insertEvent(newEvent)

            } else {
                // --- CASO 2: EDITAR EVENTO EXISTENTE ---
                // Primero recuperamos el evento original para no perder su ID ni su userId original
                val existingEvent = repository.getEventById(eventId)

                existingEvent?.let { event ->
                    // Creamos una copia con los datos nuevos del formulario
                    val eventToUpdate = event.copy(
                        name = s.name,
                        location = s.location,
                        date = s.date,
                        description = s.description,
                        price = s.price.toDoubleOrNull() ?: 0.0,
                        imageUrl = s.imageUrl.ifBlank { event.imageUrl } // Mantenemos la foto anterior si está vacía
                        // IMPORTANTE: No cambiamos el 'userId' al editar.
                        // El evento sigue perteneciendo a quien lo creó.
                    )
                    repository.updateEvent(eventToUpdate)
                }
            }

            // 3. Avisar a la UI que hemos terminado
            _uiState.update { it.copy(isSaved = true) }
        }
    }
}

class EventFormViewModelFactory(
    private val repository: EventRepository,
    private val eventId: String?
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EventFormViewModel(repository, eventId) as T
    }
}