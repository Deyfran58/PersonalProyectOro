package com.example.goldnet.util

import java.security.MessageDigest
import java.text.NumberFormat
import java.util.*

object Util {

    // Hash simple con SHA-256 (suficiente para demo; para producción usar salt + algoritmos fuertes)
    fun hashPassword(password: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val bytes = md.digest(password.toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun formatCurrency(amount: Double, locale: Locale = Locale.getDefault()): String {
        val nf = NumberFormat.getCurrencyInstance(locale)
        return nf.format(amount)
    }

    fun formatDate(timestamp: Long, locale: Locale = Locale.getDefault()): String {
        val fmt = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", locale)
        return fmt.format(Date(timestamp))
    }
}
