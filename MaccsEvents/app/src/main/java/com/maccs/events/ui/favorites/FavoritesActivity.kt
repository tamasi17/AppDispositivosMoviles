package com.maccs.events.ui.favorites

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maccs.events.MaccsEventsApp
import com.maccs.events.ui.components.AppBottomBar
import com.maccs.events.ui.home.EventCard // Reusing your existing component
import com.maccs.events.ui.theme.MaccsEventsTheme
import com.maccs.events.ui.theme.LigthPurple
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
            containerColor = Color.Black,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Mis Favoritos",
                            style = MaterialTheme.typography.titleLarge.copy(fontFamily = NunitoFamily),
                            color = LigthPurple
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Black,
                        titleContentColor = LigthPurple,
                        actionIconContentColor = LigthPurple
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
                            style = MaterialTheme.typography.bodyLarge,
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


    // --- FUNCIÓN STATELESS PARA PREVIEWS (Sin ViewModel) ---
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun FavoritesContent(
        favorites: List<com.maccs.events.data.model.Event>,
        onFavoriteToggle: (com.maccs.events.data.model.Event) -> Unit = {}
    ) {
        Scaffold(
            containerColor = Color.Black, // FONDO NEGRO PURO
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Mis Favoritos",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontFamily = NunitoFamily,
                                color = LigthPurple,
                                fontSize = 24.sp
                            )
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Black,
                        titleContentColor = LigthPurple
                    )
                )
            },
            bottomBar = {
                // Simulamos la barra inferior para la preview
                Surface(
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    color = Color(0xFF121212)
                ) {
                    Text("AppBottomBar", color = Color.Gray, modifier = Modifier.padding(16.dp))
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                if (favorites.isEmpty()) {
                    Text(
                        text = "No tienes favoritos aún",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Gray,
                        fontFamily = NunitoFamily
                    )
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(favorites) { event ->
                            EventCard(
                                event = event,
                                onClick = {},
                                onFavoriteClick = { onFavoriteToggle(event) }
                            )
                        }
                    }
                }
            }
        }
    }
}



