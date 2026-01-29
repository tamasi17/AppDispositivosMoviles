package com.maccs.events.ui.favorites

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Scaffold
import com.maccs.events.ui.components.AppBottomBar
import com.maccs.events.ui.theme.MaccsEventsTheme
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding

class FavoritesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaccsEventsTheme {
                Scaffold(
                    // Aquí llamamos a tu barra de navegación
                    bottomBar = { AppBottomBar() }
                ) { paddingValues ->
                    // Aquí llamamos a la Screen que creamos arriba
                    FavoritesScreen(modifier = Modifier.padding(paddingValues))
                }
            }
        }
    }
}

