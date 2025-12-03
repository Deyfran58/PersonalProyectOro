package com.example.projetgoldnet

data class ApiResponse(
    val success: Boolean,
    val message: String? = null,
    val user: User? = null,
    val users: List<User>? = null
)