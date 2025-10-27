package com.example.goldnet.data.repository

import com.example.goldnet.data.model.GoldPrice
import com.example.goldnet.data.model.Purchase
import com.example.goldnet.data.model.User

interface DataManager {
    // Users
    suspend fun createUser(user: User): Boolean
    suspend fun getUserById(id: String): User?
    suspend fun getUserByEmail(email: String): User?
    suspend fun authenticate(email: String, passwordPlain: String): User?

    // Gold prices
    suspend fun addGoldPrice(price: GoldPrice): Boolean
    suspend fun getLatestGoldPrice(): GoldPrice?
    suspend fun getGoldPrices(limit: Int = 50): List<GoldPrice>

    // Purchases
    suspend fun addPurchase(purchase: Purchase): Boolean
    suspend fun getPurchasesForUser(userId: String): List<Purchase>

    // Utilities
    suspend fun clearAll() // para pruebas
}
