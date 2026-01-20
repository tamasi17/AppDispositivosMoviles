package com.maccs.events.ui.event

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maccs.events.data.model.Event
import com.maccs.events.data.repository.FakeDataSource
import kotlinx.coroutines.launch

sealed interface EventDetailUiState {
    object Loading : EventDetailUiState
    data class Success(val event: Event) : EventDetailUiState
    object Error : EventDetailUiState
}

class EventDetailViewModel : ViewModel() {

    private val repository = FakeDataSource()

    // Este es el estado que la UI va a observar
    var uiState: EventDetailUiState by mutableStateOf(EventDetailUiState.Loading)
        private set

    // Función para cargar el evento por ID
    fun loadEvent(eventId: String) {
        viewModelScope.launch {
            uiState = EventDetailUiState.Loading
            val event = repository.getEventById(eventId)
            uiState = if (event != null) {
                EventDetailUiState.Success(event)
            } else {
                EventDetailUiState.Error
            }
        }
    }

    // Función para marcar/desmarcar favorito
    fun toggleFavorite(eventId: String) {
        viewModelScope.launch {
            repository.toggleFavorite(eventId)
            // Recargamos para que la UI vea el cambio
            loadEvent(eventId)
        }
    }

    // Función para confirmar/cancelar asistencia
    fun toggleAttendance(eventId: String) {
        viewModelScope.launch {
            repository.toggleAttendance(eventId)
            // Recargamos para que la UI vea el cambio
            loadEvent(eventId)
        }
    }
}