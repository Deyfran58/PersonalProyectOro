package com.example.goldnet

import androidx.media3.common.util.Util

class UserController(private val userManager: MemoryDataManager<User>) {

    fun register(name: String, email: String, password: String): User {
        require(Util.equals(email)) { "Invalid email" }
        val id = Util.generateId()
        val hash = Util.hashPassword(password)
        val user = User(id, name, email, hash)
        userManager.add(user)
        return user
    }

    fun login(email: String, password: String): User? {
        return userManager.getAll().firstOrNull {
            it.email.equals(email, ignoreCase = true) &&
                    Util.hashPassword(password) == it.passwordHash
        }
    }
}
