package com.maccs.events

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.maccs.events.ui.auth.LoginActivity
import com.maccs.events.ui.home.HomeActivity // Asumiendo que existe

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // AQUÍ VA TU LÓGICA DE ENRUTAMIENTO

        // 1. (Opcional) Comprobar si hay usuario guardado (Simulado por ahora)
        val isUserLoggedIn = false

        val intent = if (isUserLoggedIn) {
            // Si ya está logueado, vamos directo al Home
            Intent(this, HomeActivity::class.java)
        } else {
            // Si no, vamos al Login
            Intent(this, LoginActivity::class.java)
        }

        // 2. Arrancar la actividad
        startActivity(intent)

        // 3. ¡IMPORTANTE! Matar la MainActivity para que el usuario
        // no pueda volver a ella pulsando "Atrás"
        finish()
    }
}