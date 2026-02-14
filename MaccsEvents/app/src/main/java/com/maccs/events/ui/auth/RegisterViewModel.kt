package com.maccs.events.ui.auth

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.maccs.events.data.repository.AuthRepository
import kotlinx.coroutines.launch
import com.maccs.events.ui.utils.FileHelper

class RegisterViewModel(private val repository: AuthRepository) : ViewModel() {

    // Usamos mutableStateOf para que Compose reaccione directamente a los cambios de estado
    var uiState by mutableStateOf<RegisterUiState>(RegisterUiState.Idle)
        private set

    fun onRegister(context: Context, email: String, pass: String, name: String, uri: Uri?) {
        // Evitamos registros duplicados si ya está cargando
        if (uiState is RegisterUiState.Loading) return

        viewModelScope.launch {
            uiState = RegisterUiState.Loading

            // 1. Procesar imagen localmente (si existe)
            val internalPath = uri?.let {
                FileHelper.saveImageToInternalStorage(
                    context,
                    it,
                    "profile_${System.currentTimeMillis()}"
                )
            }

            // 2. Llamar al repositorio (Firebase + Room)
            val result = repository.registerUser(email, pass, name, internalPath)

            // 3. Actualizar estado según el resultado
            uiState = if (result.isSuccess) {
                RegisterUiState.Success
            } else {
                RegisterUiState.Error(result.exceptionOrNull()?.message ?: "Error desconocido")
            }
        }
    }

    // Función útil para limpiar errores antes de volver a intentar
    fun resetState() {
        uiState = RegisterUiState.Idle
    }
}

// --- ESTADOS DE LA UI ---
sealed class RegisterUiState {
    object Idle : RegisterUiState()
    object Loading : RegisterUiState()
    object Success : RegisterUiState()
    data class Error(val msg: String) : RegisterUiState()
}

// --- FACTORY (Lo que te faltaba para inyectar el AuthRepository) ---
class RegisterViewModelFactory(private val repository: AuthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}