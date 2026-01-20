package com.maccs.events.ui.event

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    eventId: String,
    viewModel: EventDetailViewModel,
    onBack: () -> Unit
) {
    // Al iniciar, le pedimos al ViewModel que cargue el evento
    LaunchedEffect(eventId) {
        viewModel.loadEvent(eventId)
    }

    val state = viewModel.uiState

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Evento") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            when (state) {
                is EventDetailUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is EventDetailUiState.Error -> {
                    Text("Error al cargar el evento", modifier = Modifier.align(Alignment.Center))
                }
                is EventDetailUiState.Success -> {
                    val event = state.event

                    // Contenido con Scroll
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        // Imagen del evento
                        AsyncImage(
                            model = event.imageUrl,
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth().height(250.dp),
                            contentScale = ContentScale.Crop
                        )

                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = event.name, style = MaterialTheme.typography.headlineMedium)

                            Spacer(modifier = Modifier.height(8.dp))

                            Row {
                                Icon(Icons.Default.LocationOn, contentDescription = null)
                                Text(text = event.location, style = MaterialTheme.typography.bodyLarge)
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(text = "Descripción", style = MaterialTheme.typography.titleLarge)
                            Text(text = event.longDescription, style = MaterialTheme.typography.bodyMedium)

                            Spacer(modifier = Modifier.height(24.dp))

                            // Botones de acción
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                                // Botón Favorito
                                OutlinedButton(onClick = { viewModel.toggleFavorite(event.id) }) {
                                    Icon(
                                        imageVector = if (event.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = null
                                    )
                                    Text(if (event.isFavorite) "Quitar Favorito" else "Añadir Favorito")
                                }

                                // Botón Asistir
                                Button(onClick = { viewModel.toggleAttendance(event.id) }) {
                                    Text(if (event.isAttending) "No asistiré" else "¡Asistiré!")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}