package com.maccs.events.ui.profile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maccs.events.R
import com.maccs.events.ui.theme.*

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Black
            ) {
                ProfileScreen()
            }
        }
    }
}

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título "Mi Perfil"
        Text(
            text = "Mi Perfil",
            color = LigthPurple,
            fontSize = 28.sp, // Un poco más grande para que resalte
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 40.dp)
        )

        // Imagen de perfil circular
        Box(
            modifier = Modifier
                .size(160.dp)
                .border(2.dp, White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.profile_icon_svg),
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                tint = White
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Campo: Nombre
        ProfileTextField(label = "Nombre", isEnabled = true, borderColor = LigthPurple)

        Spacer(modifier = Modifier.height(16.dp))

        // Campo: Mail
        ProfileTextField(label = "Mail", isEnabled = true, borderColor = Color.Gray)

        Spacer(modifier = Modifier.height(16.dp))

        // Campo: ID (No editable)
        ProfileTextField(label = "ID (no editable)", isEnabled = false, borderColor = Color.Gray)

        Spacer(modifier = Modifier.height(24.dp))

        // Botón Guardar (Alineado a la derecha)
        Button(
            onClick = { /* Lógica de guardado */ },
            modifier = Modifier
                .align(Alignment.End)
                .width(120.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Black),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, LigthPurple)
        ) {
            Text("Guardar", color = White)
        }

        Spacer(modifier = Modifier.weight(1f))

        // Botón Cerrar Sesión (Abajo con borde rojo)
        OutlinedButton(
            onClick = { /* Lógica de logout */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.Red),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = White)
        ) {
            Text("Cerrar sesión", fontSize = 18.sp)
        }
    }
}

@Composable
fun ProfileTextField(label: String, isEnabled: Boolean, borderColor: Color) {
    OutlinedTextField(
        value = "", // Aquí iría el estado de tu ViewModel
        onValueChange = {},
        enabled = isEnabled,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label, color = White) },
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = borderColor,
            unfocusedBorderColor = borderColor,
            disabledBorderColor = borderColor,
            focusedTextColor = White,
            unfocusedTextColor = White,
            disabledTextColor = Color.Gray
        )
    )
}