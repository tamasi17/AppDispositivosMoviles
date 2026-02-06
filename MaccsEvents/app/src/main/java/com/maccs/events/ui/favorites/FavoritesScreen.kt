package com.maccs.events.ui.favorites

import androidx.compose.ui.tooling.preview.Preview
import android.content.res.Configuration
import androidx.compose.material3.Surface
import com.maccs.events.ui.theme.MaccsEventsTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.isSystemInDarkTheme
import com.maccs.events.ui.theme.LigthPurple
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maccs.events.data.Evento
import com.maccs.events.data.EventoRepository
import com.maccs.events.ui.event.CreateEventActivity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.draw.clip
import com.maccs.events.R
import com.maccs.events.ui.components.AppBottomBar

@Composable
fun FavoritesScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val favoritos by EventoRepository.eventosFavoritos.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            // CAMBIO: Ahora usa el fondo del tema (blanco en light, negro en dark)
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = "Mis Favoritos",
            // CAMBIO: Color morado por defecto del tema
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
                        onCardClick = { /* ... tu lógica de intent ... */ },
                        onRemoveClick = { EventoRepository.eliminarFavorito(evento.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun EventoFavoritoCard(evento: Evento, onCardClick: () -> Unit, onRemoveClick: () -> Unit) {
    val isDark = isSystemInDarkTheme()

    Card(
        colors = CardDefaults.cardColors(
            // CAMBIO: Gris oscuro en modo noche, gris muy clarito en modo día
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
                        text = evento.titulo,
                        // CAMBIO: Texto negro en modo claro, blanco en oscuro
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = onRemoveClick,
                        modifier = Modifier.size(32.dp) // Aumenta el área de toque y visualización
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.fav_icon_filled),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = LigthPurple
                        )
                    }
                }
                Text(
                    text = evento.fecha,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(
    name = "Pantalla Completa - Claro",
    showBackground = true,
    group = "Screens"
)
@Preview(
    name = "Pantalla Completa - Oscuro",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    group = "Screens"
)
@Composable
fun FavoritesScreenPreview() {
    MaccsEventsTheme {
        // Surface asegura que el fondo cambie según el modo (Blanco/Negro)
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            FavoritesScreen()
        }
    }
}

@Preview(
    name = "Tarjeta Evento - Claro",
    showBackground = true,
    group = "Components"
)
@Preview(
    name = "Tarjeta Evento - Oscuro",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    group = "Components"
)
@Composable
fun EventoCardPreview() {
    MaccsEventsTheme {
        Surface {
            Box(modifier = Modifier.padding(16.dp)) {
                EventoFavoritoCard(
                    evento = Evento(
                        id = 1,
                        titulo = "Concierto Rock Urbano",
                        fecha = "15/02/2026 - 20:00h",
                        lugar = "Sala La Riviera"
                    ),
                    onCardClick = {},
                    onRemoveClick = {}
                )
            }
        }
    }
}

@Preview(
    name = "Lista Vacía - Claro",
    showBackground = true,
    group = "Screens"
)
@Preview(
    name = "Lista Vacía - Oscuro",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    group = "Screens"
)
@Composable
fun FavoritesEmptyPreview() {
    MaccsEventsTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            // Aquí forzamos una versión de la pantalla que no tenga datos
            // Si usas el Repositorio, podrías simularlo o simplemente llamar
            // a una versión del Composable que reciba una lista vacía.
            FavoritesContentEmptyState()
        }
    }
}

@Composable
fun FavoritesContentEmptyState() {
    // Reutilizamos la estructura de tu columna pero pasando una lista vacía
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Mis Favoritos",
            color = Color(0xFF8A2BE2), // Tu morado de proyecto
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Aún no tienes eventos favoritos",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
@Preview(
    name = "Pantalla con Barra - Claro",
    showBackground = true
)
@Preview(
    name = "Pantalla con Barra - Oscuro",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun FavoritesWithBarPreview() {
    MaccsEventsTheme {
        // Simulamos la estructura de la Activity dentro de la Preview
        Scaffold(
            bottomBar = { AppBottomBar() }
        ) { paddingValues ->
            // Le pasamos el padding para que la barra no tape el contenido
            FavoritesScreen(modifier = Modifier.padding(paddingValues))
        }
    }
}