package com.maccs.events.ui.event

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.maccs.events.ui.theme.MaccsEventsTheme

class CreateEventActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Configuración de pantalla completa
        enableEdgeToEdge()

        // 2. Obtención de parámetros (ID para modo edición)
        val eventId = intent.getStringExtra("EVENT_ID")

        setContent {
            // 3. heredar los estilos correctos
            MaccsEventsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black
                ) {
                    EventFormScreen(eventId = eventId)
                }
            }
        }
    }
}