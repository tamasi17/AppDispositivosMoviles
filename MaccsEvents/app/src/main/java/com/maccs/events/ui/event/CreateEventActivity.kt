package com.maccs.events.ui.event

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

class CreateEventActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Obtenemos el ID si existe (para saber si editamos)
        val eventId = intent.getStringExtra("EVENT_ID")

        setContent {
            // Aquí llamaremos a la pantalla en el siguiente paso
            EventFormScreen(eventId = eventId)
        }
    }
}