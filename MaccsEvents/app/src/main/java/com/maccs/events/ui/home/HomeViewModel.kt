package com.maccs.events.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.maccs.events.data.model.Event
import com.maccs.events.data.repository.EventRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// DEFINICIÓN DEL ESTADO DE LA UI
data class HomeUiState(
    val isLoading: Boolean = true,
    val events: List<Event> = emptyList(),
    val searchQuery: String = "",
    val isSearching: Boolean = false
)

class HomeViewModel(private val repository: EventRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _isSearching = MutableStateFlow(false)

    // Combinamos el Flow de Room con el Flow de la búsqueda
    // Así, si cambia la DB O cambias el texto de búsqueda, la UI se actualiza sola
    val uiState: StateFlow<HomeUiState> = combine(
        repository.getEventsFlow(), // Usamos el Flow continuo de Room
        _searchQuery,
        _isSearching
    ) { events, query, searching ->
        val filteredEvents = if (query.isBlank()) {
            events
        } else {
            events.filter { it.name.contains(query, ignoreCase = true) }
        }

        HomeUiState(
            isLoading = false,
            events = filteredEvents,
            searchQuery = query,
            isSearching = searching
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState(isLoading = true)
    )

    // --- LÓGICA DE BÚSQUEDA ---
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun toggleSearchMode() {
        _isSearching.update { !it }
        if (!_isSearching.value) {
            onSearchQueryChanged("")
        }
    }

    // --- LÓGICA DE FAVORITOS ---
    fun toggleFavorite(event: Event) {
        viewModelScope.launch {
            // Usamos el ID para que el repositorio busque la entidad fresca en la DB
            repository.toggleFavorite(event.id)
        }
    }
}



// --- FACTORY ---
class HomeViewModelFactory(private val repository: EventRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}