package com.maccs.events.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.TextStyle
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
import com.maccs.events.ui.event.EventDetailScreen
import com.maccs.events.ui.event.EventDetailViewModel
import com.maccs.events.ui.event.EventDetailViewModelFactory
import com.maccs.events.ui.theme.MaccsEventsTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.ui.unit.sp
import com.maccs.events.ui.theme.LigthPurple
import com.maccs.events.ui.theme.NunitoFamily
import androidx.compose.ui.tooling.preview.Preview
import com.maccs.events.R.drawable.fav_icon_filled
import com.maccs.events.R.drawable.fav_icon

class HomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 1. CONEXIÓN CON MVVM Y ROOM
        // Obtenemos el contenedor de dependencias (donde vive la Base de Datos)
        val appContainer = (application as MaccsEventsApp).container

        // Inicializamos el ViewModel usando la Factory
        val homeViewModel: HomeViewModel by viewModels {
            HomeViewModelFactory(appContainer.eventRepository)
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

            composable("home") {
                HomeScreen(
                    viewModel = viewModel,
                    onEventClick = { eventId ->
                        // ¡ESTA LÍNEA ES LA QUE HACE LA MAGIA!
                        navController.navigate("detail/$eventId")
                    }
                )
            }

            composable(
                route = "detail/{eventId}",
                arguments = listOf(navArgument("eventId") { type = NavType.StringType })
            ) { backStackEntry ->
                // 1. Recuperamos el ID
                val eventId = backStackEntry.arguments?.getString("eventId") ?: return@composable

                // 2. Obtenemos el contexto y el contenedor
                val context = androidx.compose.ui.platform.LocalContext.current
                val appContainer = (context.applicationContext as MaccsEventsApp).container

                // 3. Creamos el ViewModel con la Factory (Paso crítico)
                // Asegúrate de importar: androidx.lifecycle.viewmodel.compose.viewModel
                val detailViewModel: EventDetailViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = EventDetailViewModelFactory(appContainer.eventRepository, eventId)
                )

                // 4. Mostramos la pantalla
                EventDetailScreen(
                    eventId = eventId,
                    viewModel = detailViewModel,
                    onBack = { navController.popBackStack() }, // Volver atrás
                    onEditClick = { idToEdit ->
                        // Usamos 'context' que definimos arriba
                        val intent = android.content.Intent(context, com.maccs.events.ui.event.CreateEventActivity::class.java)
                        intent.putExtra("EVENT_ID", idToEdit)
                        context.startActivity(intent)
                    }
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
                                placeholder = { Text("Buscar eventos...",
                                    color = Color.Gray,
                                    fontFamily = NunitoFamily) },
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
                            Text("Próximos Eventos",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontFamily = NunitoFamily,
                                    color = _root_ide_package_.com.maccs.events.ui.theme.LigthPurple,
                                    fontSize = 24.sp
                                )
                            )
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
                                    tint = LigthPurple
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
                        color = Color.Gray,
                        fontFamily = NunitoFamily
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
                            color = Color.Black,
                            fontFamily = NunitoFamily
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
                            style = MaterialTheme.typography.bodyMedium.copy(fontFamily = NunitoFamily),                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                        // BOTÓN FAVORITO
                        IconButton(onClick = onFavoriteClick) {
                            Icon(
                                painter = painterResource(
                                    id = if (event.isFavorite) fav_icon_filled else fav_icon
                                ),
                                contentDescription = "Favorito",
                                tint = if (event.isFavorite) com.maccs.events.ui.theme.LigthPurple else com.maccs.events.ui.theme.LigthPurple
                            )
                        }
                    }

                    Text(
                        // Usamos shortDescription si existe, o location como fallback visual
                        text = event.shortDescription ?: "",
                        style = MaterialTheme.typography.bodyMedium.copy(fontFamily = NunitoFamily),                         color = Color.LightGray,
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
                                style = MaterialTheme.typography.bodySmall.copy(fontFamily = NunitoFamily),
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
                                style = MaterialTheme.typography.labelLarge.copy(fontFamily = NunitoFamily),                                 color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

// --- DATOS PARA PREVIEWS (Basados en tu FakeDataSource) ---
object HomePreviewData {
    val events = listOf(
        Event(
            id = "1",
            name = "Tech Fest Madrid 2024",
            date = 1710493200000L,
            location = "Campus UC3M",
            price = 45.0,
            imageUrl = "https://images.unsplash.com/photo-1540575467063-178a50c2df87?q=80&w=1000",
            shortDescription = "El mayor evento de tecnología.",
            longDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit...",
            isFavorite = false,
            isAttending = true,
        ),
        Event(
            id = "2",
            name = "Concierto Jazz",
            date = 1711098000000L,
            location = "Sala Barco",
            price = 12.5,
            imageUrl = "https://images.unsplash.com/photo-1511192336575-5a79af67a629?q=80&w=1000",
            shortDescription = "Música suave al aire libre.",
            longDescription = "Disfruta de una velada inolvidable...",
            isFavorite = true,
            isAttending = false
        ),
        Event(
            id = "3",
            name = "Taller de Sushi",
            date = 1712307600000L,
            location = "Don Buri",
            price = 60.0,
            imageUrl = "https://images.unsplash.com/photo-1553621042-f6e147245754?q=80&w=1000",
            shortDescription = "Aprende con el chef Kenji.",
            longDescription = "Materiales incluidos para aprender a hacer makis...",
            isFavorite = false,
            isAttending = false
        )
    )
}

// --- TODAS LAS VARIANTES DE PREVIEW ---

@Preview(showBackground = true, name = "1. Estado: Cargando")
@Composable
fun PreviewHomeLoading() {
    MaccsEventsTheme(darkTheme = true) {
        // Simulamos el interior del Scaffold cuando state.isLoading es true
        Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
            CircularProgressIndicator(
                color = LigthPurple,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Preview(showBackground = true, name = "2. Estado: Lista Vacía")
@Composable
fun PreviewHomeEmpty() {
    MaccsEventsTheme(darkTheme = true) {
        Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
            Text(
                text = "No hay eventos disponibles",
                modifier = Modifier.align(Alignment.Center),
                color = Color.Gray,
                fontFamily = NunitoFamily
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "3. Estado: Buscando")
@Composable
fun PreviewHomeSearching() {
    MaccsEventsTheme(darkTheme = true) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        TextField(
                            value = "Jazz",
                            onValueChange = {},
                            placeholder = { Text("Buscar eventos...", color = Color.Gray, fontFamily = NunitoFamily) },
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = TextStyle(fontFamily = NunitoFamily, fontSize = 18.sp),
                            singleLine = true
                        )
                    },
                    actions = {
                        IconButton(onClick = {}) {
                            Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = LigthPurple)
                        }
                    }
                )
            }
        ) { padding ->
            Column(modifier = Modifier.padding(padding)) {
                // Mostramos solo el resultado que coincide
                EventCard(event = HomePreviewData.events[1], onClick = {}, onFavoriteClick = {})
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "4. Estado: Lista Completa (Real)")
@Composable
fun PreviewHomeFullList() {
    MaccsEventsTheme() {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("Próximos Eventos",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontFamily = NunitoFamily,
                                color = LigthPurple,
                                fontSize = 24.sp
                            )
                        )
                    },
                    actions = {
                        IconButton(onClick = {}) {
                            Icon(
                                painter = painterResource(id = R.drawable.search_icon_svg),
                                contentDescription = "Buscar",
                                modifier = Modifier.size(24.dp),
                                tint = LigthPurple
                            )
                        }
                    }
                )
            },
            bottomBar = { AppBottomBar() }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(HomePreviewData.events) { event ->
                    EventCard(
                        event = event,
                        onClick = {},
                        onFavoriteClick = {}
                    )
                }
            }
        }
    }
}