package com.maccs.events.ui.auth
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.auth.FirebaseAuth
import com.maccs.events.R
import com.maccs.events.ui.home.HomeActivity

import com.maccs.events.ui.theme.*
class LoginActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializamos Firebase
        auth = FirebaseAuth.getInstance()

        // Usamos setContent (Compose)
        setContent {
            // Llamamos a la pantalla de login
            LoginScreen(
                // Definimos qué pasa cuando el usuario pulsa el botón en la UI
                onLoginClick = { emailInput, passwordInput ->
                    realizarLoginEnFirebase(emailInput, passwordInput)
                }
            )
        }
    }

    // Separamos la lógica de login en una función privada para que quede más limpio
    private fun realizarLoginEnFirebase(email: String, pass: String) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("Firebase", "signInWithEmail:success")
                    // Navegar al Home
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Log.w("Firebase", "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Error: ${task.exception?.message}", // tira exception
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }
}

@Composable
fun LoginScreen(onLoginClick: (String, String) -> Unit) {
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
                if (email.isNotBlank() && password.isNotBlank()) {
                    // Pasamos los datos escritos a la Activity
                    onLoginClick(email, password)
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