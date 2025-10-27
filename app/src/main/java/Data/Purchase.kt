package com.example.goldnet.data.model

data class Purchase(
    val id: String,
    val userId: String,
    val grams: Double,
    val totalPrice: Double,
    val pricePerGramAtPurchase: Double,
    val createdAt: Long = System.currentTimeMillis()
)
