package com.maccs.events.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.maccs.events.data.local.dao.UserDao
import com.maccs.events.data.local.entity.UserEntity
import kotlinx.coroutines.tasks.await

class AuthRepository(private val userDao: UserDao) {
    private val auth = FirebaseAuth.getInstance()

    suspend fun registerUser(email: String, pass: String, name: String, imagePath: String?): Result<Unit> {
        return try {
            // 1. Crear usuario en Firebase Auth
            val authResult = auth.createUserWithEmailAndPassword(email, pass).await()
            val uid = authResult.user?.uid ?: throw Exception("No se pudo obtener el UID")

            // 2. Guardar datos extendidos en Room (Local)
            val userLocal = UserEntity(
                id = uid,
                name = name,
                email = email,
                profileImagePath = imagePath
            )
            userDao.insertUser(userLocal)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}