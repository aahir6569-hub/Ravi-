package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "luxury_items")
data class LuxuryItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val sellerPhone: String,
    val timestamp: Long = System.currentTimeMillis(),
    val latitude: Double? = null,
    val longitude: Double? = null,
    val materialCategory: String = "Other"
)
