package com.maccs.events.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.maccs.events.data.model.Event
import com.maccs.events.data.repository.EventRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritesViewModel(private val repository: EventRepository) : ViewModel() {

    // CORRECCIÓN: Usamos el Flow de consulta del repositorio.
    // Esto hace que la lista se actualice automáticamente cuando cambia la DB.
    val favorites: StateFlow<List<Event>> = repository.getFavoriteEvents()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // CORRECCIÓN: Pasamos el ID del evento según la firma que definimos en el repositorio.
    fun toggleFavorite(event: Event) {
        viewModelScope.launch {
            repository.toggleFavorite(event.id)
        }
    }
}

// FACTORY: El "pegamento" para inyectar el repositorio en el ViewModel.
class FavoritesViewModelFactory(private val repository: EventRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoritesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}