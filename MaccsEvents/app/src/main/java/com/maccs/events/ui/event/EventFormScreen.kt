package com.maccs.events.ui.event

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun EventFormScreen(viewModel: EventFormViewModel) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = if (state.name.isEmpty()) "Crear Evento" else "Editar Evento",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        // CAMPO NOMBRE
        OutlinedTextField(
            value = state.name,
            onValueChange = { viewModel.onNameChange(it) },
            label = { Text("Nombre del evento") },
            modifier = Modifier.fillMaxWidth()
        )

        // CAMPO UBICACIÓN
        OutlinedTextField(
            value = state.location,
            onValueChange = { viewModel.onLocationChange(it) },
            label = { Text("Ubicación") },
            modifier = Modifier.fillMaxWidth()
        )

        // CAMPO PRECIO
        OutlinedTextField(
            value = state.price,
            onValueChange = { viewModel.onPriceChange(it) },
            label = { Text("Precio (€)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        // CAMPO DESCRIPCIÓN
        OutlinedTextField(
            value = state.description,
            onValueChange = { viewModel.onDescriptionChange(it) },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Spacer(modifier = Modifier.height(16.dp))

        // BOTÓN GUARDAR
        Button(
            onClick = { viewModel.saveEvent() },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("GUARDAR")
        }
    }
}