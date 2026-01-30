package com.maccs.events.ui.favorites

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.maccs.events.ui.components.AppBottomBar // Importa tu barra ya hecha
import com.maccs.events.ui.theme.MaccsEventsTheme

class FavoritesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaccsEventsTheme {
                // El Scaffold conecta la barra con el resto de la pantalla
                Scaffold(
                    bottomBar = { AppBottomBar() }
                ) { paddingValues ->
                    // Pasamos el modifier con paddingValues para respetar el espacio de la barra
                    FavoritesScreen(modifier = Modifier.padding(paddingValues))
                }
            }
        }
    }
}

