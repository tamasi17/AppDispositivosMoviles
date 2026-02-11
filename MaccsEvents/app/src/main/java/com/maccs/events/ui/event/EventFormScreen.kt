package com.maccs.events.ui.event

import android.R
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maccs.events.ui.theme.LigthPurple
import com.maccs.events.ui.theme.NunitoFamily

@Composable
fun EventFormScreen(viewModel: EventFormViewModel) {
    val state by viewModel.uiState.collectAsState()
    // Pasamos el estado y las funciones del ViewModel al contenido
    EventFormContent(
        state = state,
        onNameChange = { viewModel.onNameChange(it) },
        onLocationChange = { viewModel.onLocationChange(it) },
        onPriceChange = { viewModel.onPriceChange(it) },
        onDescriptionChange = { viewModel.onDescriptionChange(it) },
        onSave = { viewModel.saveEvent() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventFormContent(
    state: EventFormUiState,
    onNameChange: (String) -> Unit,
    onLocationChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onSave: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (state.name.isEmpty()) "Crear Evento" else "Editar Evento",
                        style = MaterialTheme.typography.titleLarge.copy(fontFamily = NunitoFamily),
                        color = LigthPurple
                    )
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- CAMPO NOMBRE ---
            OutlinedTextField(
                value = state.name,
                onValueChange = onNameChange,
                label = { Text("Nombre del evento", fontFamily = NunitoFamily) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(fontFamily = NunitoFamily)
            )

            // --- CAMPO UBICACIÓN ---
            OutlinedTextField(
                value = state.location,
                onValueChange = onLocationChange,
                label = { Text("Ubicación", fontFamily = NunitoFamily) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(fontFamily = NunitoFamily)
            )

            // --- CAMPO PRECIO ---
            OutlinedTextField(
                value = state.price,
                onValueChange = onPriceChange,
                label = { Text("Precio (€)", fontFamily = NunitoFamily) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(fontFamily = NunitoFamily)
            )

            // --- CAMPO DESCRIPCIÓN ---
            OutlinedTextField(
                value = state.description,
                onValueChange = onDescriptionChange,
                label = { Text("Descripción", fontFamily = NunitoFamily) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                textStyle = TextStyle(fontFamily = NunitoFamily)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- BOTÓN GUARDAR (ESTILO NUNITO + LIGHTPURPLE) ---
            Button(
                onClick = onSave,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LigthPurple,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "GUARDAR",
                    style = TextStyle(
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp
                    )
                )
            }
        }
    }
}