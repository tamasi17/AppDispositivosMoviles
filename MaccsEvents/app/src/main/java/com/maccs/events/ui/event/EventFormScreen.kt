package com.maccs.events.ui.event

import android.R
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maccs.events.ui.theme.LigthPurple
import com.maccs.events.ui.theme.MaccsEventsTheme
import com.maccs.events.ui.theme.NunitoFamily

@Composable
fun EventFormScreen(viewModel: EventFormViewModel, modifier: Modifier = Modifier) {
    val state by viewModel.uiState.collectAsState()

    EventFormContent(
        state = state,
        onNameChange = { viewModel.onNameChange(it) },
        onLocationChange = { viewModel.onLocationChange(it) },
        onPriceChange = { viewModel.onPriceChange(it) },
        onDescriptionChange = { viewModel.onDescriptionChange(it) },
        onSave = { viewModel.saveEvent() },
        modifier = modifier // Pasamos el padding recibido del Scaffold de la Activity
    )
}

@Composable
fun EventFormContent(
    state: EventFormUiState,
    onNameChange: (String) -> Unit,
    onLocationChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Definimos colores una vez para limpiar el código
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = LigthPurple,
        unfocusedBorderColor = Color.Gray,
        focusedLabelColor = LigthPurple,
        unfocusedLabelColor = Color.LightGray,
        cursorColor = LigthPurple,
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White
    )

    // USAMOS COLUMN DIRECTAMENTE (sin otro Scaffold)
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- CAMPO NOMBRE ---
        OutlinedTextField(
            value = state.name,
            onValueChange = onNameChange,
            label = { Text("Nombre del evento", fontFamily = NunitoFamily) },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(fontFamily = NunitoFamily),
            colors = textFieldColors
        )

        // --- CAMPO UBICACIÓN ---
        OutlinedTextField(
            value = state.location,
            onValueChange = onLocationChange,
            label = { Text("Ubicación", fontFamily = NunitoFamily) },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(fontFamily = NunitoFamily),
            colors = textFieldColors
        )

        // --- CAMPO PRECIO ---
        OutlinedTextField(
            value = state.price,
            onValueChange = onPriceChange,
            label = { Text("Precio (€)", fontFamily = NunitoFamily) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(fontFamily = NunitoFamily),
            colors = textFieldColors
        )

        // --- CAMPO DESCRIPCIÓN ---
        OutlinedTextField(
            value = state.description,
            onValueChange = onDescriptionChange,
            label = { Text("Descripción", fontFamily = NunitoFamily) },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            textStyle = TextStyle(fontFamily = NunitoFamily),
            colors = textFieldColors
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onSave,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LigthPurple, contentColor = Color.Black),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("GUARDAR", style = TextStyle(fontFamily = NunitoFamily, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp))
        }
    }
}

@Preview(showBackground = true, name = "Formulario - Nuevo Evento")
@Composable
fun PreviewFormNew() {
    MaccsEventsTheme(darkTheme = false) {
        // Estado vacío
        EventFormContent(
            state = EventFormUiState(),
            onNameChange = {}, onLocationChange = {}, onPriceChange = {}, onDescriptionChange = {}, onSave = {}
        )
    }
}

@Preview(showBackground = true, name = "Formulario - Editando Evento")
@Composable
fun PreviewFormEdit() {
    MaccsEventsTheme(darkTheme = false) {
        // Estado con datos
        EventFormContent(
            state = EventFormUiState(
                name = "Concierto Jazz",
                location = "Sala Barco",
                price = "15.0",
                description = "Una noche de música relajante."
            ),
            onNameChange = {}, onLocationChange = {}, onPriceChange = {}, onDescriptionChange = {}, onSave = {}
        )
    }
}

@Preview(showBackground = true, name = "Formulario - Modo Oscuro", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewFormDarkMode() {
    MaccsEventsTheme (darkTheme = true) {
        EventFormContent(
            state = EventFormUiState(name = "Taller de Sushi"),
            onNameChange = {}, onLocationChange = {}, onPriceChange = {}, onDescriptionChange = {}, onSave = {}
        )
    }
}