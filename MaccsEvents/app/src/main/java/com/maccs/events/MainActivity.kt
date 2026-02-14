package com.maccs.events

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth
import com.maccs.events.data.local.AppDatabase
import com.maccs.events.ui.auth.LoginActivity
import com.maccs.events.ui.home.HomeActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // --- LO QUE FALTA: Inicializar Room ---
        // Esto asegura que la DB esté lista para cualquier Activity que venga después
        AppDatabase.getDatabase(this)

        val currentUser = FirebaseAuth.getInstance().currentUser
        val intent = if (currentUser != null) {
            Intent(this, HomeActivity::class.java)
        } else {
            Intent(this, LoginActivity::class.java)
        }

        startActivity(intent)
        finish()
    }
}