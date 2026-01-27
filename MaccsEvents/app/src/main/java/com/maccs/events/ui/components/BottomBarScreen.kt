package com.maccs.events.ui.components

import android.content.Intent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.maccs.events.R
import com.maccs.events.ui.home.HomeActivity
import com.maccs.events.ui.event.CreateEventActivity
import com.maccs.events.ui.home.SearchEventActivity
import com.maccs.events.ui.profile.ProfileActivity

sealed class BottomBarScreen(
    val icon: Int, // Solo un icono por pantalla
    val activityClass: Class<*>
) {
    object Home : BottomBarScreen(R.drawable.calendar_icon_svg, HomeActivity::class.java)
    object Create : BottomBarScreen(R.drawable.plus_icon_svg, CreateEventActivity::class.java)
    object Favorites : BottomBarScreen(R.drawable.fav_icon, SearchEventActivity::class.java)
    object Profile : BottomBarScreen(R.drawable.profile_icon_svg, ProfileActivity::class.java)
}