package com.example.goldnet.data.model

data class User(
    val id: String,              // uuid
    val name: String,
    val email: String,
    val passwordHash: String,    // nunca guardar texto plano
    val createdAt: Long = System.currentTimeMillis()
)
