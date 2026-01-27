package com.maccs.events.ui.fav

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.maccs.events.ui.components.AppBottomBar
import com.maccs.events.ui.theme.MaccsEventsTheme

class FavoritesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaccsEventsTheme {
                // Usamos Scaffold para que tu AppBottomBar se quede fija abajo
                Scaffold(
                    bottomBar = { AppBottomBar() },
                    containerColor = MaterialTheme.colorScheme.background
                ) { paddingValues ->
                    // El contenido de la pantalla
                    FavoritesContent(Modifier.padding(paddingValues))
                }
            }
        }
    }
}

@Composable
fun FavoritesContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Mis Eventos Favoritos",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Tu lista está vacía por ahora",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}