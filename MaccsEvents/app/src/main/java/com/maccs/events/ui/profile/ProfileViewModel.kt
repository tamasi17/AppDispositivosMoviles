package com.maccs.events.ui.profile

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.maccs.events.data.local.dao.UserDao
import com.maccs.events.data.local.entity.UserEntity
import kotlinx.coroutines.launch

class ProfileViewModel(private val userDao: UserDao) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val currentUser = auth.currentUser

    // El ID de Firebase es nuestra clave para Room
    val idNoEditable = currentUser?.uid ?: "Sin ID"

    // Estados para la UI
    var nombre by mutableStateOf("")
    var mail by mutableStateOf("")
    var imageUri by mutableStateOf<Uri?>(null)
    var isEditable by mutableStateOf(false)

    init {
        cargarDatosDesdeRoom()
    }

    private fun cargarDatosDesdeRoom() {
        viewModelScope.launch {
            val user = userDao.getUserById(idNoEditable)
            user?.let {
                nombre = it.name
                mail = it.email
                // Si el path no es nulo, lo convertimos a Uri
                imageUri = it.profileImagePath?.let { path -> Uri.parse(path) }
            }
        }
    }

    fun onNombreChange(newValue: String) { nombre = newValue }
    fun onMailChange(newValue: String) { mail = newValue }
    fun onImageSelected(uri: Uri?) { imageUri = uri }

    fun toggleEdit() {
        // Si estamos cancelando la edición (isEditable es true y va a pasar a false)
        // recargamos los datos originales para descartar cambios no guardados
        if (isEditable) {
            cargarDatosDesdeRoom()
        }
        isEditable = !isEditable
    }

    fun guardarPerfil() {
        viewModelScope.launch {
            val user = UserEntity(
                id = idNoEditable,
                name = nombre,
                email = mail,
                profileImagePath = imageUri?.toString()
            )
            // Room insertUser debería usar OnConflictStrategy.REPLACE en el DAO
            userDao.insertUser(user)
            isEditable = false
        }
    }

    fun cerrarSesion(onSuccess: () -> Unit) {
        auth.signOut()
        onSuccess()
    }
}

// --- FACTORY ---
class ProfileViewModelFactory(private val userDao: UserDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(userDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}