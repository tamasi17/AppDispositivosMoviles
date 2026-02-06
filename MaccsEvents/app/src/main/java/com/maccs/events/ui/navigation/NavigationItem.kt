package com.maccs.events.ui.navigation

import android.app.Activity
import com.maccs.events.R
import com.maccs.events.ui.event.CreateEventActivity
import com.maccs.events.ui.favorites.FavoritesActivity
import com.maccs.events.ui.home.HomeActivity
import com.maccs.events.ui.profile.ProfileActivity
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text

sealed class NavigationItem(
    val route: String,
    val title: String,
    val icon: Int,
    val activityClass: Class<out Activity>
) {
    object Home : NavigationItem("home", "Inicio", R.drawable.calendar_icon_svg, HomeActivity::class.java)
    object Favorites : NavigationItem("favorites", "Favoritos", R.drawable.fav_icon, FavoritesActivity::class.java)
    object CreateEvent : NavigationItem("create_event", "Crear", R.drawable.plus_icon_svg, CreateEventActivity::class.java)
    object Profile : NavigationItem("profile", "Perfil", R.drawable.profile_icon_svg, ProfileActivity::class.java)
}