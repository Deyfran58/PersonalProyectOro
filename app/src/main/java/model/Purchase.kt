package com.example.goldnet

data class Purchase(
    val id: String,
    val userId: String,
    val productId: String,
    val quantity: Int
)
