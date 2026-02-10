package com.maccs.events

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth
import com.maccs.events.ui.auth.LoginActivity
import com.maccs.events.ui.home.HomeActivity // Asumiendo que existe

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Preguntamos a Firebase si existe el usuario de test
        val currentUser = FirebaseAuth.getInstance().currentUser

        val intent = if (currentUser != null) {
            // USUARIO LOGUEADO -> Vamos al Home
            Intent(this, HomeActivity::class.java)
        } else {
            // NADIE LOGUEADO -> Vamos al Login
            Intent(this, LoginActivity::class.java)
        }

        // 2. Iniciamos la actividad correspondiente
        startActivity(intent)

        // 3. ¡IMPORTANTE! Matamos esta Activity para que el usuario no pueda volver atrás
        finish()
    }
}