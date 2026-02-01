package com.maccs.events.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maccs.events.data.model.Event
import com.maccs.events.data.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<List<Event>>(emptyList())
    val uiState: StateFlow<List<Event>> = _uiState.asStateFlow()

    init {
        loadFavorites()
    }

    // Carga todos los eventos y filtra solo los favoritos
    fun loadFavorites() {
        viewModelScope.launch {
            val allEvents = EventRepository.EventRepository.getEvents()
            // Filtramos en el ViewModel
            _uiState.value = allEvents.filter { it.isFavorite }
        }
    }

    fun removeFavorite(eventId: String) {
        viewModelScope.launch {
            // 1. Avisamos al repo que cambie el estado
            EventRepository.EventRepository.toggleFavorite(eventId)
            // 2. Recargamos la lista para que desaparezca de la pantalla
            loadFavorites()
        }
    }
}