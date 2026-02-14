package com.maccs.events.ui.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.maccs.events.data.model.Event
import com.maccs.events.data.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// 1. DEFINICIÓN DE ESTADOS
sealed interface EventDetailUiState {
    object Loading : EventDetailUiState
    data class Error(val message: String) : EventDetailUiState
    data class Success(val event: Event) : EventDetailUiState
}

class EventDetailViewModel(
    private val repository: EventRepository,
    private val eventId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow<EventDetailUiState>(EventDetailUiState.Loading)
    val uiState: StateFlow<EventDetailUiState> = _uiState.asStateFlow()

    private val _isDeleted = MutableStateFlow(false)
    val isDeleted = _isDeleted.asStateFlow()

    init {
        loadEvent()
    }

    private fun loadEvent() {
        viewModelScope.launch {
            _uiState.value = EventDetailUiState.Loading

            // Obtenemos el evento por ID desde Room
            val event = repository.getEventById(eventId)
            if (event != null) {
                _uiState.value = EventDetailUiState.Success(event)
            } else {
                _uiState.value = EventDetailUiState.Error("Evento no encontrado o eliminado")
            }
        }
    }

    // TOGGLE FAVORITO
    fun toggleFavorite() {
        val currentState = _uiState.value
        if (currentState is EventDetailUiState.Success) {
            viewModelScope.launch {
                // Usamos el ID como definimos en el Repositorio corregido
                repository.toggleFavorite(eventId)

                // Opción A: Volver a pedir el objeto para refrescar la pantalla
                val updatedEvent = repository.getEventById(eventId)
                if (updatedEvent != null) {
                    _uiState.value = EventDetailUiState.Success(updatedEvent)
                }
            }
        }
    }

    // FUNCIÓN DE BORRADO
    fun deleteEvent() {
        val currentState = _uiState.value
        if (currentState is EventDetailUiState.Success) {
            val eventToDelete = currentState.event
            viewModelScope.launch {
                // Usamos el repositorio real para borrar de Room
                repository.deleteEvent(eventToDelete)
                // Avisamos a la UI para que cierre la pantalla de detalle y vuelva al Home
                _isDeleted.value = true
            }
        }
    }
}

// --- FACTORY ---
class EventDetailViewModelFactory(
    private val repository: EventRepository,
    private val eventId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EventDetailViewModel(repository, eventId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}