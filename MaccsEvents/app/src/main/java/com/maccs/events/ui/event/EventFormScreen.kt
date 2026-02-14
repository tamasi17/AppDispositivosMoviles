package com.maccs.events.ui.event

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maccs.events.ui.theme.LightPurple
import com.maccs.events.ui.theme.NunitoFamily
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventFormScreen(
    viewModel: EventFormViewModel,
    onPickImage: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    Scaffold(
        containerColor = Color.Black, // Fondo base negro
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (state.isEditing) "Editar Evento" else "Nuevo Evento",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontFamily = NunitoFamily,
                            fontWeight = FontWeight.Bold
                        ),
                        color = LightPurple
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Black
                )
            )
        }
    ) { innerPadding ->
        // Usamos Column dentro del contenido del Scaffold
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // Evita que el contenido quede bajo la TopBar
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Selector de Fecha
                Box(modifier = Modifier.weight(2f)) {
                    CustomTextField(
                        value = state.date,
                        onValueChange = { },
                        label = "dd/MM/yyyy",
                        readOnly = true
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable {
                                DatePickerDialog(
                                    context,
                                    { _, year, month, day ->
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CustomButtonField(
                    text = if (state.imageUrl.isEmpty()) "Imagen" else "¡Seleccionada!",
                    modifier = Modifier.weight(1f),
                    onClick = { onPickImage() }
                )

                CustomTextField(
                    value = state.price,
                    onValueChange = { viewModel.onPriceChange(it) },
                    label = "Precio",
                    modifier = Modifier.weight(1f),
                    keyboardType = KeyboardType.Number
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón Guardar
            Button(
                onClick = { viewModel.saveEvent() },
                modifier = Modifier
                    .align(Alignment.End)
                    .width(150.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LightPurple)
            ) {
                Text(
                    text = "GUARDAR",
                    color = Color.White,
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
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
    readOnly: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        readOnly = readOnly,
        placeholder = { Text(label, color = Color.Gray, fontFamily = NunitoFamily) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF9333EA),
            unfocusedBorderColor = borderColor,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            cursorColor = Color(0xFF9333EA)
        ),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = singleLine
    )
}

@Composable
fun CustomButtonField(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    OutlinedCard(
        onClick = onClick,
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