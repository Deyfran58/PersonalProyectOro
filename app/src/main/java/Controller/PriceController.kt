package com.goldnet.controllers

import com.example.goldnet.data.model.GoldPrice
import com.example.goldnet.data.repository.DataManager

class PriceController(private val dataManager: DataManager) {
    suspend fun getLatestPrice(): GoldPrice? = dataManager.getLatestGoldPrice()

    suspend fun pushNewPrice(value: Double) {
        val p = GoldPrice(timestamp = System.currentTimeMillis(), pricePerGram = value)
        dataManager.addGoldPrice(p)
    }

    suspend fun history(limit: Int = 50) = dataManager.getGoldPrices(limit)
}
