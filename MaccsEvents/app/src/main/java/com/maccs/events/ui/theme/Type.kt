package com.maccs.events.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.Font
import com.maccs.events.R.font

// Set of Material typography styles to start with
// ui/theme/Type.kt
val NunitoFamily = FontFamily(
    Font(font.nunito_regular, FontWeight.Normal),
    Font(font.nunito_regular, FontWeight.Bold)
)

val Typography = Typography(
    // Estilo para los títulos de las barras superiores
    titleLarge = TextStyle(
        fontFamily = NunitoFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp, // El tamaño concreto que querías
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    // Puedes añadir bodyLarge, labelSmall, etc.
)