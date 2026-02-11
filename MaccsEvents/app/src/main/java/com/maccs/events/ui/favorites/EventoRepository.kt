package com.maccs.events.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class Evento(val id: Int, val titulo: String, val fecha: String, val lugar: String, var isFavorite: Boolean = true)

object EventoRepository {
    private val _eventosFavoritos = MutableStateFlow<List<Evento>>(
        listOf(Evento(1, "Concierto Rock Urbano", "15/02/2026 - 20:00h", "Sala La Riviera"))
    )
    val eventosFavoritos: StateFlow<List<Evento>> = _eventosFavoritos

    fun eliminarFavorito(id: Int) {
        _eventosFavoritos.value = _eventosFavoritos.value.filter { it.id != id }
    }
}