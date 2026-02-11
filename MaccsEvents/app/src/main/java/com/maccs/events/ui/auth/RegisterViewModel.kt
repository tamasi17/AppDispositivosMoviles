package com.maccs.events.ui.auth

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maccs.events.data.repository.AuthRepository
import kotlinx.coroutines.launch
import  com.maccs.events.ui.utils.FileHelper


class RegisterViewModel(private val repository: AuthRepository) : ViewModel() {

    var uiState by mutableStateOf<RegisterUiState>(RegisterUiState.Idle)
        private set

    fun onRegister(context: Context, email: String, pass: String, name: String, uri: Uri?) {
        viewModelScope.launch {
            uiState = RegisterUiState.Loading

            // 1. Procesar imagen localmente primero
            val internalPath = uri?.let {
                FileHelper.saveImageToInternalStorage(context, it, "profile_${System.currentTimeMillis()}")
            }

            // 2. Llamar al repositorio
            val result = repository.registerUser(email, pass, name, internalPath)

            uiState = if (result.isSuccess) {
                RegisterUiState.Success
            } else {
                RegisterUiState.Error(result.exceptionOrNull()?.message ?: "Error desconocido")
            }
        }
    }
}

sealed class RegisterUiState {
    object Idle : RegisterUiState()
    object Loading : RegisterUiState()
    object Success : RegisterUiState()
    data class Error(val msg: String) : RegisterUiState()
}