package com.example.goldnet


data class Product(
    val id: String,
    val title: String,
    val description: String,
    val karat: Int,
    val price: Double,
    val imageUrl: String? = null
)
