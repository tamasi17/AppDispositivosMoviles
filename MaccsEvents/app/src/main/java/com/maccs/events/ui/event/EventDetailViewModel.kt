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


// 1. DEFINICIÓN DE ESTADOS (Exito, Error, Cargando)
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

    init {
        loadEvent()
    }

    private fun loadEvent() {
        viewModelScope.launch {
            _uiState.value = EventDetailUiState.Loading

            val event = repository.getEventById(eventId)
            if (event != null) {
                _uiState.value = EventDetailUiState.Success(event)
            } else {
                _uiState.value = EventDetailUiState.Error("No se encontró el evento")
            }
        }
    }
    fun toggleFavorite() {
        // Para acceder al evento dentro del estado Success, usamos smart cast
        val currentState = _uiState.value
        if (currentState is EventDetailUiState.Success) {
            val event = currentState.event
            viewModelScope.launch {
                repository.toggleFavorite(event)
                // Recargamos para actualizar la UI
                loadEvent()
            }
        }
    }

    private val _isDeleted = MutableStateFlow(false)
    val isDeleted = _isDeleted.asStateFlow()

    // 2. FUNCIÓN DE BORRADO
    fun deleteEvent() {
        val currentState = _uiState.value
        if (currentState is EventDetailUiState.Success) {
            val eventToDelete = currentState.event
            viewModelScope.launch {
                repository.deleteEvent(eventToDelete)
                // Avisamos a la UI que el borrado ha terminado
                _isDeleted.value = true
            }
        }
    }


}

// --- FACTORY: Necesaria para pasar ID y Repo al ViewModel ---
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