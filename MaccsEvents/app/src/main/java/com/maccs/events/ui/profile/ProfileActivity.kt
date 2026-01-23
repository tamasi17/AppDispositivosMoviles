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
        Text(
            text = "Mi Perfil",
            color = LigthPurple,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        )

        Spacer(modifier = Modifier.height(60.dp))

        // icono profile_icon_svg
        Box(
            modifier = Modifier
                .size(140.dp)
                .border(1.dp, White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.profile_icon_svg),
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = White
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("usuario@gmail.com", color = White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text("Modo: Colaborador/Usuario", color = Color.Gray, fontSize = 14.sp)

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { /* Logout */ },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LigthPurple),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("CERRAR SESIÓN", color = White, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}