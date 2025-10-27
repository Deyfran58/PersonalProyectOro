package com.example.goldnet.controllers

import com.example.goldnet.data.model.User
import com.example.goldnet.data.repository.DataManager
import com.example.goldnet.util.Util
import java.util.UUID

class UserController(private val dataManager: DataManager) {

    suspend fun register(name: String, email: String, passwordPlain: String): Result<User> {
        if (name.isBlank() || email.isBlank() || passwordPlain.length < 4) {
            return Result.failure(IllegalArgumentException("Datos inválidos"))
        }

        val existing = dataManager.getUserByEmail(email)
        if (existing != null) return Result.failure(IllegalStateException("Email ya registrado"))

        val user = User(
            id = UUID.randomUUID().toString(),
            name = name.trim(),
            email = email.trim().lowercase(),
            passwordHash = Util.hashPassword(passwordPlain)
        )
        val ok = dataManager.createUser(user)
        return if (ok) Result.success(user) else Result.failure(RuntimeException("No se pudo crear usuario"))
    }

    suspend fun login(email: String, passwordPlain: String): Result<User> {
        val user = dataManager.authenticate(email.trim().lowercase(), passwordPlain)
            ?: return Result.failure(IllegalArgumentException("Credenciales incorrectas"))
        return Result.success(user)
    }
}
