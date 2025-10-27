package com.goldnet.controllers

import com.example.goldnet.data.model.Purchase
import com.example.goldnet.data.repository.DataManager
import java.util.UUID

class PurchaseController(private val dataManager: DataManager) {

    suspend fun buy(userId: String, grams: Double): Result<Purchase> {
        if (grams <= 0.0) return Result.failure(IllegalArgumentException("Cantidad inválida"))
        val latest = dataManager.getLatestGoldPrice() ?: return Result.failure(IllegalStateException("Precio no disponible"))
        val total = grams * latest.pricePerGram
        val p = Purchase(
            id = UUID.randomUUID().toString(),
            userId = userId,
            grams = grams,
            totalPrice = total,
            pricePerGramAtPurchase = latest.pricePerGram
        )
        dataManager.addPurchase(p)
        return Result.success(p)
    }

    suspend fun getUserPurchases(userId: String) = dataManager.getPurchasesForUser(userId)
}
