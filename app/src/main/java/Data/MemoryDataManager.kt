package com.example.goldnet
class MemoryDataManager<T> {
    private val items = mutableListOf<T>()

    fun add(item: T) = items.add(item)
    fun getAll(): List<T> = items
    fun remove(item: T) = items.remove(item)
}
