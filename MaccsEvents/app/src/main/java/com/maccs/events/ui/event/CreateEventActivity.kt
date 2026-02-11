package com.maccs.events.ui.event

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.maccs.events.MaccsEventsApp
import com.maccs.events.ui.components.AppBottomBar
import com.maccs.events.ui.theme.LigthPurple
import com.maccs.events.ui.theme.MaccsEventsTheme
import com.maccs.events.ui.theme.NunitoFamily
import androidx.compose.material3.ExperimentalMaterial3Api

class CreateEventActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val eventId = intent.getStringExtra("EVENT_ID")
        val appContainer = (application as MaccsEventsApp).container
        val viewModel: EventFormViewModel by viewModels {
            EventFormViewModelFactory(appContainer.eventRepository, eventId)
        }

        setContent {
            MaccsEventsTheme(darkTheme = true) {
                val state by viewModel.uiState.collectAsState()

                LaunchedEffect(state.isSaved) {
                    if (state.isSaved) finish()
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color.Black,
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    // El título se decide aquí, igual que en Favoritos o Perfil
                                    text = if (state.name.isEmpty()) "Crear Evento" else "Editar Evento",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontFamily = NunitoFamily,
                                        fontWeight = FontWeight.Bold // Ahora será Bold por tu cambio de fuente
                                    ),
                                    color = LigthPurple
                                )
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color.Black,
                                titleContentColor = LigthPurple
                            )
                        )
                    },
                    bottomBar = { AppBottomBar() }
                ) { innerPadding ->
                    // Pasamos el modifier con el padding de la TopAppBar
                    EventFormScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}