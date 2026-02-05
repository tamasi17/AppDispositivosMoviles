package com.maccs.events.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.maccs.events.data.model.Event
import com.maccs.events.data.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// DEFINICIÓN DEL ESTADO DE LA UI
data class HomeUiState(
    val isLoading: Boolean = true,
    val events: List<Event> = emptyList(),
    val searchQuery: String = "",
    val isSearching: Boolean = false
)

class HomeViewModel(private val repository: EventRepository) : ViewModel() {

    // Usamos un StateFlow complejo para manejar carga, búsqueda y lista a la vez
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // Guardamos la lista completa para filtrar sobre ella sin machacarla
    private var fullEventList: List<Event> = emptyList()

    init {
        loadEvents()
    }

    private fun loadEvents() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // Recolectamos del repositorio real (Room)
            repository.getEvents().collect { events ->
                fullEventList = events
                // Al recibir datos nuevos, volvemos a aplicar el filtro si hay una búsqueda activa
                val currentQuery = _uiState.value.searchQuery
                val eventsToShow = if (currentQuery.isBlank()) {
                    events
                } else {
                    events.filter { it.name.contains(currentQuery, ignoreCase = true) }
                }

                _uiState.update {
                    it.copy(events = eventsToShow, isLoading = false)
                }
            }
        }
    }

    // --- LÓGICA DE BÚSQUEDA ---
    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }

        if (query.isBlank()) {
            _uiState.update { it.copy(events = fullEventList) }
        } else {
            val filtered = fullEventList.filter {
                it.name.contains(query, ignoreCase = true)
            }
            _uiState.update { it.copy(events = filtered) }
        }
    }

    fun toggleSearchMode() {
        _uiState.update { currentState ->
            val newSearchMode = !currentState.isSearching
            if (!newSearchMode) {
                // Al cerrar búsqueda, limpiamos
                onSearchQueryChanged("")
            }
            currentState.copy(isSearching = newSearchMode)
        }
    }

    // --- LÓGICA DE FAVORITOS (Rescatada del primer ViewModel) ---
    fun toggleFavorite(event: Event) {
        viewModelScope.launch {
            repository.toggleFavorite(event)
        }
    }
}

// --- FACTORY (Rescatada del primer ViewModel) ---
class HomeViewModelFactory(private val repository: EventRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}