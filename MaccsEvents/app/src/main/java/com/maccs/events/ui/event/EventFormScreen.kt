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

            var eventTitle by remember { mutableStateOf("") }
            var location by remember { mutableStateOf("") }

            Spacer(modifier = Modifier.height(20.dp))

            // Campo Título
            OutlinedTextField(
                value = eventTitle,
                onValueChange = { eventTitle = it },
                label = { Text("Título del evento") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFBB86FC),
                    unfocusedBorderColor = Color.DarkGray,
                    focusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Fila para Fecha y Hora
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = "", onValueChange = {},
                    label = { Text("dd/MM/yyyy") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = "", onValueChange = {},
                    label = { Text("Hora") },
                    modifier = Modifier.weight(0.5f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { /* Lógica para guardar */ },
                modifier = Modifier.align(androidx.compose.ui.Alignment.End),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBB86FC)),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
            ) {
                Text("Guardar", color = Color.White)
            }
        }
    }
}