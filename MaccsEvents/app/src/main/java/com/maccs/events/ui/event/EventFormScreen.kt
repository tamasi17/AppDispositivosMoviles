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

            //texto debajo del títuñp
            Text(
                text = "No pierdas la oportunidad de anunciar tu evento. Rellena los campos con la información correcta.",
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Campo Título
            OutlinedTextField(
                value = eventTitle,
                onValueChange = { eventTitle = it },
                label = { Text("Título del evento") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFBB86FC),
                    unfocusedBorderColor = Color.DarkGray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
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
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White

                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Fila para Fecha y Hora
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("dd/MM/yyyy", color = Color.Gray) },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFBB86FC),
                        unfocusedBorderColor = Color.DarkGray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text("Hora", color = Color.Gray) },
                    modifier = Modifier.weight(0.5f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFBB86FC),
                        unfocusedBorderColor = Color.DarkGray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))


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
            // Fila para Imagen y Precio
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = "Imagen",
                    onValueChange = {},
                    label = { Text("Imagen", color = Color.Gray) },
                    modifier = Modifier.weight(1f),
                    enabled = false, // Se mantiene estático como en la imagen
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledBorderColor = Color.DarkGray,
                        disabledLabelColor = Color.Gray,
                        disabledTextColor = Color.Gray
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Precio", color = Color.Gray) },
                    modifier = Modifier.weight(1f),
                    // Sugerencia: teclado numérico
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFBB86FC),
                        unfocusedBorderColor = Color.DarkGray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White // Para que el texto no "desaparezca" al perder foco
                    )
                )
            }


            Spacer(modifier = Modifier.height(20.dp))

            //botón guardar
            Button(
                onClick = { /* Lógica para guardar */ },
                modifier = Modifier.align(Alignment.End).width(120.dp).height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBB86FC)),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
            ) {
                Text("Guardar", color = Color.White)
            }
        }
    }
}