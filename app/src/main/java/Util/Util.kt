package com.example.goldnet

import java.security.MessageDigest
import java.util.UUID
import android.util.Patterns

object Util {
    fun generateId(): String = UUID.randomUUID().toString()

    fun hashPassword(password: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        return md.digest(password.toByteArray()).joinToString("") {
            "%02x".format(it)
        }
    }

    fun validateEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
