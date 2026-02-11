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


    val idNoEditable = currentUser?.uid ?: "Sin ID"


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
                imageUri = it.profileImagePath?.let { path -> Uri.parse(path) }
            }
        }
    }

    fun onNombreChange(newValue: String) { nombre = newValue }
    fun onMailChange(newValue: String) { mail = newValue }
    fun onImageSelected(uri: Uri?) { imageUri = uri }

    fun toggleEdit() {
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
            userDao.insertUser(user)
            isEditable = false
        }
    }

    fun cerrarSesion(onSuccess: () -> Unit) {
        auth.signOut()
        onSuccess()
    }
}

// Clase necesaria para pasar el UserDao al ViewModel
class ProfileViewModelFactory(private val userDao: UserDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(userDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}