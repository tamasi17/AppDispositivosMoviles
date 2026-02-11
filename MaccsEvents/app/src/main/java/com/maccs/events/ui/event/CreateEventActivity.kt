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
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
class CreateEventActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val eventId = intent.getStringExtra("EVENT_ID")
        val appContainer = (application as MaccsEventsApp).container
        val viewModel: EventFormViewModel by viewModels {
            EventFormViewModelFactory(appContainer.eventRepository, eventId)
        }

        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Si el usuario selecciona una imagen, guardamos su URI en el ViewModel
            if (uri != null) {
                viewModel.onImageUrlChange(uri.toString())
            }
        }
        setContent {
            MaccsEventsTheme {
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
                        modifier = Modifier.fillMaxSize().padding(innerPadding),
                        color = Color.Transparent

                    ) {
                        EventFormScreen(
                            viewModel = viewModel,
                            onPickImage = {
                                // Esta línea lanza el selector de fotos oficial de Android
                                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            }
                        )
                    }
                }
            }
        }
    }
}