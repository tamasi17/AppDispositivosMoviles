package com.maccs.events.ui.event

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    eventId: String,
    viewModel: EventDetailViewModel,
    onBack: () -> Unit,
    onEditClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // 1. Observamos si se ha borrado para salir de la pantalla
    val isDeleted by viewModel.isDeleted.collectAsState()

    LaunchedEffect(isDeleted) {
        if (isDeleted) {
            onBack() // Vuelve al Home automáticamente
        }
    }

    // Estado local para mostrar/ocultar el diálogo de confirmación
    var showDeleteDialog by remember { mutableStateOf(false) }

    // DIÁLOGO DE CONFIRMACIÓN
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("¿Borrar evento?") },
            text = { Text("Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteEvent()
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                ) {
                    Text("Borrar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Evento") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                },
                actions = {
                    // --- BOTÓN EDITAR (Solo visible si hay éxito cargando) ---
                    if (uiState is EventDetailUiState.Success) {
                        IconButton(onClick = { onEditClick(eventId) }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar evento",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            when (val state = uiState) {
                is EventDetailUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is EventDetailUiState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Error al cargar el evento")
                        Text(text = state.message, style = MaterialTheme.typography.bodySmall, color = Color.Red)
                    }
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
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(event.imageUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp),
                            contentScale = ContentScale.Crop
                        )

                        Column(modifier = Modifier.padding(16.dp)) {
                            // Título
                            Text(
                                text = event.name,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Ubicación
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = event.location,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // Descripción
                            Text(
                                text = "Descripción",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = event.longDescription,
                                style = MaterialTheme.typography.bodyMedium,
                                lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
                            )

                            Spacer(modifier = Modifier.height(32.dp))

                            // Botones de acción inferiores
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                // Botón Favorito
                                OutlinedButton(
                                    onClick = { viewModel.toggleFavorite() },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = if (event.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = null,
                                        tint = if (event.isFavorite) Color.Red else MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(if (event.isFavorite) "Quitar" else "Favorito")
                                }

                                // Botón Asistir (Placeholder visual)
                                Button(
                                    onClick = { /* Lógica asistir */ },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Asistiré")
                                }
                            }
                        }
                    }
                }
                // Manejo de estado nulo inicial si fuera necesario
                else -> {}
            }
        }
    }
}