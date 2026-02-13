package com.maccs.events.ui.favorites

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.maccs.events.MaccsEventsApp
import com.maccs.events.ui.components.AppBottomBar
import com.maccs.events.ui.home.EventCard // Reusing your existing component
import com.maccs.events.ui.theme.LightPurple
import com.maccs.events.ui.theme.MaccsEventsTheme
import com.maccs.events.ui.theme.NunitoFamily

class FavoritesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val appContainer = (application as MaccsEventsApp).container
        val viewModel: FavoritesViewModel by viewModels {
            FavoritesViewModelFactory(appContainer.eventRepository)
        }

        setContent {
            MaccsEventsTheme(darkTheme = true) {
                FavoritesScreen(viewModel = viewModel)
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun FavoritesScreen(viewModel: FavoritesViewModel) {
        val favorites by viewModel.favorites.collectAsState()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Mis Favoritos", fontFamily = NunitoFamily) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Black,
                        titleContentColor = LightPurple
                    )
                )
            },
            bottomBar = { AppBottomBar() }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                if (favorites.isEmpty()) {
                    // Empty State
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No tienes favoritos aún",
                            style = MaterialTheme.typography.bodyLarge.copy(fontFamily = NunitoFamily),
                            color = Color.Gray
                        )
                    }
                } else {
                    // List
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(favorites) { event ->
                            EventCard(
                                event = event,
                                onClick = {
                                    // Navigate to Detail (if needed, use Intent logic here)
                                },
                                onFavoriteClick = {
                                    // This will remove it from the list immediately
                                    // because the list is observing the DB reactively
                                    viewModel.toggleFavorite(event)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}