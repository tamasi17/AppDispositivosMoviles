package com.maccs.events.ui.event

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun EventFormScreen(eventId: String?) {
    val titleText = if (eventId == null) "Nuevo Evento" else "Editar Evento"

    var eventTitle by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
        // Añadimos verticalScroll por si los campos no caben al abrir el teclado
        Column(
            modifier = Modifier
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = titleText,
                color = Color(0xFFBB86FC),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Define un evento personal o selecciona colaborativo si eres administrador de uno",
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall
            )

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

            // Campo Localización (que faltaba poner el estado)
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Localización") },
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
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("dd/MM/yyyy") },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFFBB86FC))
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text("Hora") },
                    modifier = Modifier.weight(0.5f),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFFBB86FC))
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // --- AQUÍ VA LO NUEVO ---

            // Campo Descripción
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción / notas", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth().height(150.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFBB86FC),
                    unfocusedBorderColor = Color.DarkGray,
                    focusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Fila para Imagen y Precio
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = "Imagen",
                    onValueChange = {},
                    label = { Text("Imagen", color = Color.Gray) },
                    modifier = Modifier.weight(1f),
                    enabled = false,
                    colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color.DarkGray)
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Precio", color = Color.Gray) },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFBB86FC),
                        unfocusedBorderColor = Color.DarkGray,
                        focusedTextColor = Color.White
                    )
                )
            }

            // --- FIN DE LO NUEVO ---

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { /* Lógica para guardar */ },
                modifier = Modifier.align(Alignment.End),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBB86FC)),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
            ) {
                Text("Guardar", color = Color.White)
            }
        }
    }
}