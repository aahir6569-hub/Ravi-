package com.example.data

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class LuxuryItemRepository(private val luxuryItemDao: LuxuryItemDao) {

    fun getActiveItems(cutoffTimestamp: Long): Flow<List<LuxuryItem>> {
        return luxuryItemDao.getActiveItems(cutoffTimestamp)
    }

    fun getAllItems(): Flow<List<LuxuryItem>> {
        return luxuryItemDao.getAllItems()
    }

    suspend fun insert(item: LuxuryItem) {
        luxuryItemDao.insertItem(item)
    }

    suspend fun deleteExpired(cutoffTimestamp: Long): Int {
        return luxuryItemDao.deleteExpiredItems(cutoffTimestamp)
    }

    suspend fun deleteById(id: Int) {
        luxuryItemDao.deleteById(id)
    }

    suspend fun prepopulateIfEmpty() {
        try {
            val all = luxuryItemDao.getAllItems().first()
            if (all.isEmpty()) {
                val now = System.currentTimeMillis()
                val presets = listOf(
                    LuxuryItem(
                        title = "Calacatta Gold Marble Slab",
                        description = "Ultra-premium Italian polished marble slab featuring dramatic warm golden and charcoal-grey veining. Ideal for master bathroom accent walls or heavy waterfall kitchen countertops.",
                        price = 12500.00,
                        imageUrl = "https://images.unsplash.com/photo-1618221195710-dd6b41faaea6?auto=format&fit=crop&w=800&q=80",
                        sellerPhone = "+15553042041",
                        timestamp = now - (2 * 60 * 60 * 1000), // Posted 2 hours ago
                        latitude = 40.7128,
                        longitude = -74.0060,
                        materialCategory = "Calacatta Marble"
                    ),
                    LuxuryItem(
                        title = "Chambery Smoked Oak Planks",
                        description = "Engineered French oak wood ceiling and flooring planks. Deep charcoal carbonized finish wood with hand-scraped edges, showcasing authentic textured luxury grain patterns.",
                        price = 4200.00,
                        imageUrl = "https://images.unsplash.com/photo-1540518614846-7eded433c457?auto=format&fit=crop&w=800&q=80",
                        sellerPhone = "+15559021834",
                        timestamp = now - (5 * 60 * 60 * 1000), // Posted 5 hours ago
                        latitude = 34.0522,
                        longitude = -118.2437,
                        materialCategory = "Smoked Oak"
                    ),
                    LuxuryItem(
                        title = "Fluted Silver Travertine Column",
                        description = "Solid natural Roman travertine column support unit, meticulously honed with custom micro-fluted texture bands. Adds historical grandeur and structural elegance to luxury entrances.",
                        price = 8900.00,
                        imageUrl = "https://images.unsplash.com/photo-1600585154526-990dced4db0d?auto=format&fit=crop&w=800&q=80",
                        sellerPhone = "+15558317290",
                        timestamp = now - (10 * 60 * 60 * 1000), // Posted 10 hours ago
                        latitude = 41.8781,
                        longitude = -87.6298,
                        materialCategory = "Honed Travertine"
                    ),
                    LuxuryItem(
                        title = "Champagne Anodized Gold Fittings",
                        description = "Full designer bathroom faucet and cabinet hardware assembly, finished in double-anodized brushed champagne gold. Exceptional corrosion resistance and solid brass coreweight.",
                        price = 1800.00,
                        imageUrl = "https://images.unsplash.com/photo-1600607687939-ce8a6c25118c?auto=format&fit=crop&w=800&q=80",
                        sellerPhone = "+15554501092",
                        timestamp = now - (18 * 60 * 60 * 1000), // Posted 18 hours ago
                        latitude = 25.7617,
                        longitude = -80.1918,
                        materialCategory = "Polished Brass"
                    )
                )
                for (item in presets) {
                    luxuryItemDao.insertItem(item)
                }
                Log.d("LuxuryRepository", "Successfully pre-populated luxury items")
            }
        } catch (e: Exception) {
            Log.e("LuxuryRepository", "Failed to pre-populate database", e)
        }
    }
}
