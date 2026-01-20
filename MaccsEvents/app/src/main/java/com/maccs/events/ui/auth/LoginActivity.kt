package com.maccs.events.ui.auth
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.maccs.events.R

import com.maccs.events.ui.theme.*
class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Black
            ) {
                LoginScreen(
                    onLoginSuccess = {
                        // por ahora, solo aparecerá un mensaje
                        Toast.makeText(this, "Login exitoso", Toast.LENGTH_SHORT).show()
                        // Aquí iría el Intent para abrir la HomeActivity más adelante
                    }
                )
            }
        }
    }
}

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    // 2. Gestión de Estado (State)
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "INICIAR SESIÓN",
            color = LigthPurple,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(40.dp))

        //campo del email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email", color = White.copy(alpha = 0.5f)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = LigthPurple,
                unfocusedBorderColor = White.copy(alpha = 0.3f),
                focusedTextColor = White,
                unfocusedTextColor = White,
                cursorColor = LigthPurple
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // campo de la constraseña
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña", color = White.copy(alpha = 0.5f)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(), // Oculta caracteres (****)
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = LigthPurple,
                unfocusedBorderColor = White.copy(alpha = 0.3f),
                focusedTextColor = White,
                unfocusedTextColor = White,
                cursorColor = LigthPurple
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // botón de entrar
        Button(
            onClick = {
                // Criterio de aceptación: Solo funciona si ambos tienen texto
                if (email.isNotBlank() && password.isNotBlank()) {
                    onLoginSuccess()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = LigthPurple,
                contentColor = White
            ),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = "ENTRAR",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // texto debajo del botón de entrar
        TextButton(onClick = { /* Pendiente: Registro */ }) {
            Text(
                text = "¿No tienes cuenta? Regístrate",
                color = White.copy(alpha = 0.7f),
                fontSize = 14.sp
            )
        }
    }
}