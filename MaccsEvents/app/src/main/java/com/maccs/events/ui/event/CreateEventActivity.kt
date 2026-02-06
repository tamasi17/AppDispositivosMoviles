package com.maccs.events.ui.event

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.maccs.events.MaccsEventsApp
import com.maccs.events.ui.components.AppBottomBar
import com.maccs.events.ui.theme.MaccsEventsTheme

class CreateEventActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 1. Recuperar ID
        val eventId = intent.getStringExtra("EVENT_ID")

        // 2. Crear ViewModel
        val appContainer = (application as MaccsEventsApp).container
        val viewModel: EventFormViewModel by viewModels {
            EventFormViewModelFactory(appContainer.eventRepository, eventId)
        }

        setContent {
            MaccsEventsTheme {
                // Observar si se ha guardado para cerrar
                val state by viewModel.uiState.collectAsState()
                LaunchedEffect(state.isSaved) {
                    if (state.isSaved) finish()
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color.Black,
                    bottomBar = { AppBottomBar() }
                ) { innerPadding ->

                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = Color.Transparent
                    ) {

                        EventFormScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }
}