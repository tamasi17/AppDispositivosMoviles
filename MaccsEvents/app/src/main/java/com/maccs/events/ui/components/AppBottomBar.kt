package com.maccs.events.ui.components

import android.app.Activity
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import android.content.res.Configuration
import com.maccs.events.ui.theme.MaccsEventsTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.res.painterResource
import android.content.Intent
import androidx.compose.foundation.border
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import com.maccs.events.ui.theme.LightPurple
import com.maccs.events.ui.navigation.NavigationItem

@Composable
fun AppBottomBar() {
    val context = LocalContext.current
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.CreateEvent,
        NavigationItem.Favorites,
        NavigationItem.Profile
    )

    val isDark = isSystemInDarkTheme()

    val barShape = RoundedCornerShape(20.dp)

    NavigationBar(
        modifier = Modifier
            .padding(8.dp) // Añadimos padding para que se vea el redondeado inferior
            .border(
                width = 2.dp,
                color = LightPurple,
                shape = barShape    // <--- ESTO REDONDEA EL BORDE
            )
            .clip(barShape),        // <--- ESTO REDONDEA EL CONTENIDO/FONDO
        containerColor = if (isDark) Color.Black else Color.White,
        tonalElevation = 0.dp
    ) {
        items.forEach { screen ->
            val isSelected = context.javaClass == screen.activityClass

            NavigationBarItem(
                selected = isSelected,
                label = null,
                alwaysShowLabel = false,
                icon = {
                    Icon(
                        painter = painterResource(id = screen.icon),
                        contentDescription = null,
                        // Iconos un poco más pequeños y con más espacio a los lados
                        modifier = Modifier
                            .size(40.dp)
                            .padding(horizontal = 2.dp),
                        tint = if (isSelected) {
                            LightPurple
                        } else {
                            if (isDark) Color.White else Color.Black
                        }
                    )
                },
                onClick = {
                    if (!isSelected) {
                        val intent = Intent(context, screen.activityClass)
                        // REORDER_TO_FRONT: Evita crear copias infinitas de las activities
                        intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                        context.startActivity(intent)

                        // Eliminamos la animación de transición entre Activities
                        // para que se sienta como una navegación nativa de pestañas
                        (context as? Activity)?.overridePendingTransition(0, 0)
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

@Preview(
    name = "Modo Claro",
    showBackground = true
)
@Preview(
    name = "Modo Oscuro",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun AppBottomBarPreview() {
    MaccsEventsTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            // Llama a la función sin pasarle ningún parámetro
            AppBottomBar()
        }
    }
}