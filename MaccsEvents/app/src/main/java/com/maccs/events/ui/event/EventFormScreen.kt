package com.maccs.events.ui.event

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun EventFormScreen(eventId: String?) {
    val titleText = if (eventId == null) "Nuevo Evento" else "Editar Evento"

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black // Fondo negro según tu Figma
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = titleText,
                color = Color(0xFFBB86FC), // Morado neón
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Define un evento personal o selecciona colaborativo si eres administrador de uno",
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}