package com.maccs.events.ui.event

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EventFormScreen(viewModel: EventFormViewModel) {
    val state by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(24.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Título dinámico según el estado
        Text(
            text = if (state.isEditing) "Editar Evento" else "Nuevo Evento",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF9333EA) // LigthPurple
        )

        Text(
            text = "Define un evento personal o selecciona colaborativo si eres administrador de uno",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo Título
        CustomTextField(
            value = state.name,
            onValueChange = { viewModel.onNameChange(it) },
            label = "Título del evento",
            borderColor = Color(0xFF9333EA)
        )

        // Campo Localización
        CustomTextField(
            value = state.location,
            onValueChange = { viewModel.onLocationChange(it) },
            label = "Localización"
        )

        // Fila Fecha y Hora
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            CustomTextField(
                value = state.date,
                onValueChange = { viewModel.onDateChange(it) },
                label = "dd/MM/yyyy",
                modifier = Modifier.weight(2f)
            )
            CustomTextField(
                value = state.time,
                onValueChange = { viewModel.onTimeChange(it) },
                label = "Hora",
                modifier = Modifier.weight(1f)
            )
        }

        // Campo Descripción
        CustomTextField(
            value = state.description,
            onValueChange = { viewModel.onDescriptionChange(it) },
            label = "Descripción / notas",
            modifier = Modifier.height(150.dp),
            singleLine = false
        )

        // Fila Imagen y Precio
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            // Unimos el diseño con la lógica de click y el texto dinámico
            CustomButtonField(
                text = if (state.imageUrl.isEmpty()) "Imagen" else "¡Imagen lista!",
                modifier = Modifier.weight(1f),
                onClick = {
                    // Simulamos que se añade la URL
                    viewModel.onImageUrlChange("https://picsum.photos/400/200")
                }
            )

            CustomTextField(
                value = state.price,
                onValueChange = { viewModel.onPriceChange(it) },
                label = "Precio",
                modifier = Modifier.weight(1f),
                keyboardType = KeyboardType.Number
            )
        }



        Spacer(modifier = Modifier.weight(1f))

        // Botón Guardar
        Button(
            onClick = { viewModel.saveEvent() },
            modifier = Modifier
                .align(Alignment.End)
                .width(120.dp)
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            border = BorderStroke(1.dp, Color(0xFF9333EA))
        ) {
            Text("Guardar", color = Color.White)
        }
    }
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    borderColor: Color = Color.Gray.copy(alpha = 0.5f),
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(label, color = Color.Gray) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF9333EA),
            unfocusedBorderColor = borderColor,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = singleLine
    )
}

@Composable
fun CustomButtonField(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {} // Añadimos el parámetro onClick
) {
    OutlinedCard(
        onClick = onClick, // Ahora el Card reacciona al click
        modifier = modifier.height(56.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.5f)),
        colors = CardDefaults.outlinedCardColors(containerColor = Color.Transparent)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = text, color = Color.Gray)
        }
    }
}