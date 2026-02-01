package com.maccs.events.ui.favorites

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.maccs.events.R
import com.maccs.events.data.model.Event
import com.maccs.events.ui.components.AppBottomBar // Asegúrate de importar tu barra
import com.maccs.events.ui.theme.LigthPurple
import com.maccs.events.ui.theme.MaccsEventsTheme
import java.text.SimpleDateFormat
import java.util.*

// ----------------------------------------------------------------
// 1. COMPOSABLE CON ESTADO (Stateful)
// Este se usa en la navegación real de la app.
// ----------------------------------------------------------------
@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    viewModel: FavoritesViewModel = viewModel()
) {
    val favoritos by viewModel.uiState.collectAsState()

    // Recargar al entrar
    LaunchedEffect(Unit) {
        viewModel.loadFavorites()
    }

    // Llamamos al contenido "tonto" (stateless) pasándole los datos
    FavoritesContent(
        modifier = modifier,
        favoritos = favoritos,
        onRemoveClick = { id -> viewModel.removeFavorite(id) },
        onEventClick = { /* Navegación al detalle */ }
    )
}

// ----------------------------------------------------------------
// 2. COMPOSABLE SIN ESTADO (Stateless)
// Este es el que recibe datos puros y funciones. ES EL QUE PREVISUALIZAMOS.
// ----------------------------------------------------------------
@Composable
fun FavoritesContent(
    modifier: Modifier = Modifier,
    favoritos: List<Event>,
    onRemoveClick: (String) -> Unit,
    onEventClick: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = "Mis Favoritos",
            color = LigthPurple,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (favoritos.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Aún no tienes eventos favoritos",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(favoritos) { evento ->
                    EventoFavoritoCard(
                        evento = evento,
                        onCardClick = { onEventClick(evento.id) },
                        onRemoveClick = { onRemoveClick(evento.id) }
                    )
                }
            }
        }
    }
}

// ----------------------------------------------------------------
// 3. COMPONENTE TARJETA
// ----------------------------------------------------------------
@Composable
fun EventoFavoritoCard(
    evento: Event,
    onCardClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    val isDark = isSystemInDarkTheme()

    val fechaFormateada = remember(evento.date) {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        sdf.format(Date(evento.date))
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) Color(0xFF1E1E1E) else Color(0xFFF5F5F5)
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() }
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = evento.name,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = onRemoveClick,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.fav_icon_filled),
                            contentDescription = "Eliminar favorito",
                            modifier = Modifier.size(24.dp),
                            tint = LigthPurple
                        )
                    }
                }
                Text(
                    text = "$fechaFormateada - ${evento.time}h",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = evento.location,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

// ----------------------------------------------------------------
// DATOS FAKE PARA PREVIEWS (Solo se usan aquí abajo)
// ----------------------------------------------------------------
private val sampleEvents = listOf(
    Event(
        id = "1", name = "Tech Fest Madrid", date = 1710493200000L, time = "10:00",
        location = "Campus UC3M", price = 45.0, imageUrl = "", shortDescription = "",
        longDescription = "", isFavorite = true, isAttending = true
    ),
    Event(
        id = "2", name = "Concierto Jazz", date = 1711098000000L, time = "19:30",
        location = "Sala Clamores", price = 20.0, imageUrl = "", shortDescription = "",
        longDescription = "", isFavorite = true, isAttending = false
    ),
    Event(
        id = "3", name = "Feria del Libro", date = 1712307600000L, time = "12:00",
        location = "El Retiro", price = 0.0, imageUrl = "", shortDescription = "",
        longDescription = "", isFavorite = true, isAttending = false
    )
)

// ----------------------------------------------------------------
// PREVIEWS
// ----------------------------------------------------------------

// --- GRUPO 1: TARJETAS SUELTAS ---
@Preview(name = "Tarjeta - Claro", showBackground = true, group = "Components")
@Preview(name = "Tarjeta - Oscuro", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, group = "Components")
@Composable
fun PreviewCard() {
    MaccsEventsTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            EventoFavoritoCard(
                evento = sampleEvents[0],
                onCardClick = {},
                onRemoveClick = {}
            )
        }
    }
}

// --- GRUPO 2: PANTALLA COMPLETA (CONTENIDO) ---
@Preview(name = "Contenido - Claro", showBackground = true, group = "Screens")
@Preview(name = "Contenido - Oscuro", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, group = "Screens")
@Composable
fun PreviewFavoritesContent() {
    MaccsEventsTheme {
        // Usamos FavoritesContent para poder pasarle la lista falsa manualmente
        FavoritesContent(
            favoritos = sampleEvents,
            onRemoveClick = {},
            onEventClick = {}
        )
    }
}

@Preview(name = "Contenido Vacío", showBackground = true, group = "Screens")
@Composable
fun PreviewFavoritesEmpty() {
    MaccsEventsTheme {
        FavoritesContent(
            favoritos = emptyList(),
            onRemoveClick = {},
            onEventClick = {}
        )
    }
}

// --- GRUPO 3: PANTALLA COMPLETA + BARRA INFERIOR ---
@Preview(name = "App Completa - Claro", showBackground = true, group = "App Integration")
@Preview(name = "App Completa - Oscuro", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, group = "App Integration")
@Composable
fun PreviewWithBottomBar() {
    MaccsEventsTheme {
        Scaffold(
            bottomBar = { AppBottomBar() } // Tu barra inferior real
        ) { paddingValues ->
            // Le pasamos el padding del Scaffold para que la lista no quede tapada por la barra
            FavoritesContent(
                modifier = Modifier.padding(paddingValues),
                favoritos = sampleEvents,
                onRemoveClick = {},
                onEventClick = {}
            )
        }
    }
}