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
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.ui.platform.LocalContext
import java.util.*
import androidx.compose.foundation.clickable
import com.maccs.events.ui.theme.LightPurple
import com.maccs.events.ui.theme.NunitoFamily

@Composable
fun EventFormScreen(viewModel: EventFormViewModel,
                    onPickImage: () -> Unit) {
    val state by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

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
            color = LightPurple,
            fontFamily = NunitoFamily
        )

        Text(
            text = "Define un evento personal o selecciona colaborativo si eres administrador de uno",
            fontSize = 14.sp,
            color = Color.Gray,
            fontFamily = NunitoFamily
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

            // Selector de Fecha
            Box(modifier = Modifier.weight(2f)) {
                CustomTextField(
                    value = state.date,
                    onValueChange = { },
                    label = "dd/MM/yyyy",
                    readOnly = true
                )
                // Capa clicable encima
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable {
                            DatePickerDialog(
                                context,
                                { _, year, month, day ->
                                    // Formateamos con %02d para que siempre tenga 2 dígitos (ej: 05/09/2026)
                                    val dateFormatted = String.format("%02d/%02d/%04d", day, month + 1, year)
                                    viewModel.onDateChange(dateFormatted)
                                },
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                            ).show()
                        }
                )
            }

            // Selector de Hora
            Box(modifier = Modifier.weight(1f)) {
                CustomTextField(
                    value = state.time,
                    onValueChange = { },
                    label = "Hora",
                    readOnly = true
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable {
                            TimePickerDialog(
                                context,
                                { _, hour, minute ->
                                    viewModel.onTimeChange(String.format("%02d:%02d", hour, minute))
                                },
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                true
                            ).show()
                        }
                )
            }
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
            CustomButtonField(
                text = if (state.imageUrl.isEmpty()) "Imagen" else "¡Imagen seleccionada!",
                modifier = Modifier.weight(1f),
                onClick = { onPickImage() } // <-- Ahora llama a la galería real
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
            Text("Guardar", color = Color.White, fontFamily = NunitoFamily)
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
    singleLine: Boolean = true,
    readOnly: Boolean = false // Añadimos este parámetro
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        readOnly = readOnly, // Aplicamos el readOnly aquí
        placeholder = { Text(label, color = Color.Gray, fontFamily = NunitoFamily) },
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
            Text(text = text, color = Color.Gray, fontFamily = NunitoFamily)
        }
    }
}