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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import java.util.*

// Import your theme elements
import com.maccs.events.ui.theme.LigthPurple
import com.maccs.events.ui.theme.MaccsEventsTheme
import com.maccs.events.ui.theme.NunitoFamily

@Composable
fun EventFormScreen(
    viewModel: EventFormViewModel,
    modifier: Modifier = Modifier,
    onPickImage: () -> Unit // Kept from DEV (needed for functionality)
) {
    val state by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    
    // We pass the modifier from the Scaffold to handle padding correctly
    EventFormContent(
        state = state,
        onNameChange = { viewModel.onNameChange(it) },
        onLocationChange = { viewModel.onLocationChange(it) },
        onPriceChange = { viewModel.onPriceChange(it) },
        onDescriptionChange = { viewModel.onDescriptionChange(it) },
        onDateChange = { viewModel.onDateChange(it) }, // Added from DEV
        onTimeChange = { viewModel.onTimeChange(it) }, // Added from DEV
        onPickImage = onPickImage,
        onSave = { viewModel.saveEvent() },
        modifier = modifier
    )
}

@Composable
fun EventFormContent(
    state: EventFormUiState,
    onNameChange: (String) -> Unit,
    onLocationChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onDateChange: (String) -> Unit,
    onTimeChange: (String) -> Unit,
    onPickImage: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(scrollState)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // --- TITLE (Structure from DEV, Style from FONT) ---
        Text(
            text = if (state.isEditing) "Editar Evento" else "Nuevo Evento",
            fontSize = 28.sp,
            fontFamily = NunitoFamily, // Added Font
            fontWeight = FontWeight.Bold,
            color = LigthPurple // Added Color
        )

        Text(
            text = "Define un evento personal o selecciona colaborativo si eres administrador de uno",
            fontSize = 14.sp,
            fontFamily = NunitoFamily, // Added Font
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(8.dp))

        // --- FIELDS (Using the CustomTextField helper from DEV) ---

        CustomTextField(
            value = state.name,
            onValueChange = onNameChange,
            label = "Título del evento"
        )

        CustomTextField(
            value = state.location,
            onValueChange = onLocationChange,
            label = "Localización"
        )

        // --- DATE & TIME ROW (Logic from DEV) ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Date Picker
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
                                    val dateFormatted =
                                        String.format("%02d/%02d/%04d", day, month + 1, year)
                                    onDateChange(dateFormatted)
                                },
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                            ).show()
                        }
                )
            }

            // Time Picker
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
                                    onTimeChange(String.format("%02d:%02d", hour, minute))
                                },
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                true
                            ).show()
                        }
                )
            }
        }

        CustomTextField(
            value = state.description,
            onValueChange = onDescriptionChange,
            label = "Descripción / notas",
            modifier = Modifier.height(150.dp),
            singleLine = false
        )

        // --- IMAGE & PRICE ROW ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CustomButtonField(
                text = if (state.imageUrl.isEmpty()) "Imagen" else "¡Imagen seleccionada!",
                modifier = Modifier.weight(1f),
                onClick = onPickImage
            )

            CustomTextField(
                value = state.price,
                onValueChange = onPriceChange,
                label = "Precio",
                modifier = Modifier.weight(1f),
                keyboardType = KeyboardType.Number
            )
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
        // Aplicamos la fuente Nunito al placeholder
        placeholder = { Text(label, color = Color.Gray, fontFamily = NunitoFamily) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = LigthPurple, // Tu color morado
            unfocusedBorderColor = borderColor,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            cursorColor = LigthPurple
        ),
        // Aplicamos la fuente Nunito al texto que escribes
        textStyle = TextStyle(fontFamily = NunitoFamily),
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
            Text(
                text = text,
                color = Color.Gray,
                fontFamily = NunitoFamily // Tu fuente
            )
        }
    }
}