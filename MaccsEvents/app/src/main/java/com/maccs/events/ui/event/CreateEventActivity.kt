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
import com.maccs.events.ui.components.AppBottomBar
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.layout.padding

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
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color.Black,
                    bottomBar = {
                        // añadir la barra inferior
                        AppBottomBar()
                    }
                ) { innerPadding ->

                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = Color.Transparent // Para que se vea el color del Scaffold
                    ) {
                        EventFormScreen(eventId = eventId)
                    }
                }
            }
        }
    }
}