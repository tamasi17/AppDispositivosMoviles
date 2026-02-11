package com.maccs.events.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.maccs.events.R

// Definimos la familia de fuentes
val NunitoFamily = FontFamily(
    // Al poner FontWeight.Normal vinculado al archivo bold.ttf,
    // engañamos al sistema para que use Bold siempre.
    Font(R.font.nunito_semibold, FontWeight.Normal),
    Font(R.font.nunito_extrabold, FontWeight.Bold),
    Font(R.font.nunito_semibold, FontWeight.Medium)
)

// Configuramos la tipografía base de Material3
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = NunitoFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = NunitoFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = NunitoFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)