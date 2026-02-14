package com.maccs.events.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.room.Room
import com.maccs.events.data.local.AppDatabase
import com.maccs.events.data.repository.AuthRepository
import com.maccs.events.ui.home.HomeActivity

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicialización manual (Sin Hilt/Dagger por ahora)
        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "events_db")
            .fallbackToDestructiveMigration() // Importante al cambiar versión de base de datos
            .build()

        val repository = AuthRepository(db.userDao())
        val viewModel = RegisterViewModel(repository)

        setContent {
            RegisterScreen(
                viewModel = viewModel,
                onSuccess = {
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                },onLoginClick = {
                    // Creamos el Intent para ir a LoginActivity
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
            )
        }
    }
}