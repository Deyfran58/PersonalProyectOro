package com.example.goldnet.data.repository

import com.example.goldnet.data.model.GoldPrice
import com.example.goldnet.data.model.Purchase
import com.example.goldnet.data.model.User
import com.example.goldnet.util.Util
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.UUID

class MemoryDataManager : DataManager {
    // estructuras en memoria
    private val users = mutableListOf<User>()
    private val prices = mutableListOf<GoldPrice>()
    private val purchases = mutableListOf<Purchase>()

    // sincronización simple
    private val mutex = Mutex()

    override suspend fun createUser(user: User): Boolean = mutex.withLock {
        if (users.any { it.email.equals(user.email, ignoreCase = true) }) return false
        users.add(user.copy(id = user.id.ifEmpty { UUID.randomUUID().toString() }))
        return true
    }

    override suspend fun getUserById(id: String): User? = mutex.withLock {
        users.find { it.id == id }
    }

    override suspend fun getUserByEmail(email: String): User? = mutex.withLock {
        users.find { it.email.equals(email, ignoreCase = true) }
    }

    override suspend fun authenticate(email: String, passwordPlain: String): User? = mutex.withLock {
        val u = users.find { it.email.equals(email, ignoreCase = true) } ?: return null
        val hash = Util.hashPassword(passwordPlain)
        if (u.passwordHash == hash) u else null
    }

    override suspend fun addGoldPrice(price: GoldPrice) = mutex.withLock {
        prices.add(price)
        // opcional: mantener orden descendente o límite
    }

    override suspend fun getLatestGoldPrice(): GoldPrice? = mutex.withLock {
        prices.maxByOrNull { it.timestamp }
    }

    override suspend fun getGoldPrices(limit: Int): List<GoldPrice> = mutex.withLock {
        prices.sortedByDescending { it.timestamp }.take(limit)
    }

    override suspend fun addPurchase(purchase: Purchase) = mutex.withLock {
        purchases.add(purchase)
    }

    override suspend fun getPurchasesForUser(userId: String): List<Purchase> = mutex.withLock {
        purchases.filter { it.userId == userId }.sortedByDescending { it.createdAt }
    }

    override suspend fun clearAll() = mutex.withLock {
        users.clear()
        prices.clear()
        purchases.clear()
    }

    // helper para pruebas: crear datos demo
    suspend fun seedDemoData() = mutex.withLock {
        clearAll()
        val u = User(
            id = UUID.randomUUID().toString(),
            name = "Demo User",
            email = "demo@goldnet.com",
            passwordHash = Util.hashPassword("123456")
        )
        users.add(u)

        prices.add(GoldPrice(timestamp = System.currentTimeMillis(), pricePerGram = 60.0))
        prices.add(GoldPrice(timestamp = System.currentTimeMillis() - 86_400_000L, pricePerGram = 59.0))
    }
}
