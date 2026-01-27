package com.maccs.events.ui.components

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
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.size
@Composable
fun AppBottomBar() {
    val context = LocalContext.current
    val items = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Create,
        BottomBarScreen.Favorites,
        BottomBarScreen.Profile
    )

    val isDark = isSystemInDarkTheme()

    NavigationBar(
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
                            MaterialTheme.colorScheme.primary
                        } else {
                            if (isDark) Color.White else Color.Black
                        }
                    )
                },
                onClick = {
                    if (!isSelected) {
                        val intent = Intent(context, screen.activityClass)
                        intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                        context.startActivity(intent)
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
    // Ya no necesitas crear el navController aquí
    MaccsEventsTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            // Llama a la función sin pasarle ningún parámetro
            AppBottomBar()
        }
    }
}