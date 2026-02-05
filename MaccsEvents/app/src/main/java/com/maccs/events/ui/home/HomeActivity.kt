package com.maccs.events.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.maccs.events.MaccsEventsApp
import com.maccs.events.R
import com.maccs.events.data.model.Event
import com.maccs.events.ui.components.AppBottomBar
import com.maccs.events.ui.event.EventDetailViewModel
import com.maccs.events.ui.event.EventDetailViewModelFactory
import com.maccs.events.ui.theme.MaccsEventsTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 1. CONEXIÓN CON MVVM Y ROOM
        // Obtenemos el contenedor de dependencias (donde vive la Base de Datos)
        val appContainer = (application as MaccsEventsApp).container

        // Inicializamos el ViewModel usando la Factory
        val homeViewModel: HomeViewModel by viewModels {
            HomeViewModelFactory(appContainer)
        }

        setContent {
            MaccsEventsTheme(darkTheme = true, dynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Pasamos el ViewModel ya conectado a la navegación
                    MainAppNavigation(homeViewModel)
                }
            }
        }
    }

    @Composable
    fun MainAppNavigation(viewModel: HomeViewModel) {
        // Usamos NavController para la navegación interna (Lista -> Detalle)
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = "home") {

            // PANTALLA PRINCIPAL
            composable("home") {
                HomeScreen(
                    viewModel = viewModel,
                    onEventClick = { eventId ->
                        // Navegar al detalle pasando el ID
                        navController.navigate("detail/$eventId")
                    }
                )
            }

            // PANTALLA DE DETALLE
            // ... dentro de NavHost ...

            composable(
                route = "detail/{eventId}",
                arguments = listOf(navArgument("eventId") { type = NavType.StringType })
            ) { backStackEntry ->
                // 1. Recuperamos el ID de la navegación
                val eventId = backStackEntry.arguments?.getString("eventId") ?: return@composable

                // 2. Obtenemos el contenedor de dependencias (para acceder a Room)
                val context = androidx.compose.ui.platform.LocalContext.current
                val appContainer = (context.applicationContext as MaccsEventsApp).container

                // 3. Creamos el ViewModel ESPECÍFICO para este ID usando la Factory
                val detailViewModel: EventDetailViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = EventDetailViewModelFactory(appContainer, eventId)
                )

                // 4. Mostramos tu pantalla real
                EventDetailScreen(
                    eventId = eventId,
                    viewModel = detailViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun HomeScreen(
        viewModel: HomeViewModel,
        onEventClick: (String) -> Unit
    ) {
        // 2. OBSERVAMOS LA BASE DE DATOS (ROOM)
        // 'collectAsState' hace que la UI se redibuje automáticamente cuando la DB cambie
        val state by viewModel.uiState.collectAsState()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        if (state.isSearching) {
                            TextField(
                                value = state.searchQuery,
                                // Conectamos el input del usuario con el ViewModel
                                onValueChange = { viewModel.onSearchQueryChanged(it) },
                                placeholder = { Text("Buscar eventos...", color = Color.Gray) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                colors = TextFieldDefaults.colors(
                                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    cursorColor = MaterialTheme.colorScheme.primary
                                )
                            )
                        } else {
                            Text("PRÓXIMOS EVENTOS", fontWeight = FontWeight.ExtraBold)
                        }
                    },
                    actions = {
                        IconButton(onClick = { viewModel.toggleSearchMode() }) {
                            if (state.isSearching) {
                                Icon(Icons.Default.Close, contentDescription = "Cerrar")
                            } else {
                                Icon(
                                    painter = painterResource(id = R.drawable.search_icon_svg),
                                    contentDescription = "Buscar",
                                    modifier = Modifier.size(24.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.onBackground
                    )
                )
            },
            bottomBar = {
                AppBottomBar()
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else if (state.events.isEmpty()) {
                    Text(
                        text = "No hay eventos disponibles",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Gray
                    )
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(top = 8.dp, bottom = 80.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(state.events) { event ->
                            EventCard(
                                event = event,
                                onClick = { onEventClick(event.id) },
                                // Acción de favoritos conectada al ViewModel
                                onFavoriteClick = { viewModel.toggleFavorite(event) }
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun EventCard(
        event: Event,
        onClick: () -> Unit,
        onFavoriteClick: () -> Unit // Nuevo parámetro para el botón de corazón
    ) {
        val dateFormatter = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }
        val dateString = dateFormatter.format(Date(event.date))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clickable { onClick() },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column {
                // IMAGEN
                Box(modifier = Modifier.height(180.dp).fillMaxWidth()) {
                    AsyncImage(
                        model = event.imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                        contentScale = ContentScale.Crop
                    )
                    // PRECIO
                    Surface(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(bottomStart = 8.dp),
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Text(
                            text = "${event.price} €",
                            modifier = Modifier.padding(8.dp),
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }

                // CONTENIDO
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = event.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                        // BOTÓN FAVORITO
                        IconButton(onClick = onFavoriteClick) {
                            Icon(
                                imageVector = if (event.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = "Favorito",
                                tint = if (event.isFavorite) Color.Red else Color.Gray
                            )
                        }
                    }

                    Text(
                        // Usamos shortDescription si existe, o location como fallback visual
                        text = event.shortDescription ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.LightGray,
                        maxLines = 1
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Ubicación",
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = event.location,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }

                        Surface(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = dateString,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}