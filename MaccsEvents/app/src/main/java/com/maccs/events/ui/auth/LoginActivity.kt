package com.maccs.events.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.maccs.events.R
import com.maccs.events.ui.home.HomeActivity
import com.maccs.events.ui.theme.*

/**
 * 1. LOGIN ACTIVITY
 */
class LoginActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        setContent {
            LoginScreen(
                onLoginClick = { emailInput, passwordInput ->
                    realizarLoginEnFirebase(emailInput, passwordInput)
                },onRegisterClick = {
                    // Creamos el Intent para ir a RegisterActivity
                    val intent = Intent(this, RegisterActivity::class.java)
                    startActivity(intent)
                }
            )
        }
    }

    private fun realizarLoginEnFirebase(email: String, pass: String) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("Firebase", "signInWithEmail:success")
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Log.w("Firebase", "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Error: ${task.exception?.message}",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }
}

@Composable
fun LoginScreen(onLoginClick: (String, String) -> Unit,
                onRegisterClick: () -> Unit) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // 2. Gestión de Estado (State)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // --- LOGO IMPLEMENTADO ---
        Image(
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = "Logo de Eventos",
            modifier = Modifier
                .fillMaxWidth(0.8f) // Ocupa el 80% del ancho disponible
                .height(100.dp),     // Altura fija para mantener el formato rectangular
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "INICIAR SESIÓN",
            color = LigthPurple,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = NunitoFamily
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Campo del email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email", color = White.copy(alpha = 0.5f), fontFamily = NunitoFamily) },
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

        // Campo de la contraseña
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña", color = White.copy(alpha = 0.5f), fontFamily = NunitoFamily) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
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

        // Botón de entrar
        Button(
            onClick = {
                if (email.isNotBlank() && password.isNotBlank()) {
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
                fontWeight = FontWeight.Bold,
                fontFamily = NunitoFamily
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // texto debajo del botón de entrar
        TextButton(onClick = { onRegisterClick()}) {
            Text(
                text = "¿No tienes cuenta? Regístrate",
                color = White.copy(alpha = 0.7f),
                fontSize = 14.sp,
                fontFamily = NunitoFamily
            )
        }
    }
}

