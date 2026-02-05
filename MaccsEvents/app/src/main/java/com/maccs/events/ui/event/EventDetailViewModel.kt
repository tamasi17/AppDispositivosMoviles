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

class EventDetailViewModel(
    private val repository: EventRepository,
    private val eventId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow<Event?>(null)
    val uiState: StateFlow<Event?> = _uiState.asStateFlow()

    init {
        // Cargar el evento nada más iniciar
        viewModelScope.launch {
            val event = repository.getEventById(eventId)
            _uiState.value = event
        }
    }

    fun toggleFavorite() {
        val currentEvent = _uiState.value ?: return
        viewModelScope.launch {
            repository.toggleFavorite(currentEvent)
            // Recargamos el dato actualizado de la DB
            val updatedEvent = repository.getEventById(eventId)
            _uiState.value = updatedEvent
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