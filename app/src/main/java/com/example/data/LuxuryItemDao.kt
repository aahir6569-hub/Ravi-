package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LuxuryItemDao {
    @Query("SELECT * FROM luxury_items ORDER BY timestamp DESC")
    fun getAllItems(): Flow<List<LuxuryItem>>

    @Query("SELECT * FROM luxury_items WHERE timestamp >= :cutoffTimestamp ORDER BY timestamp DESC")
    fun getActiveItems(cutoffTimestamp: Long): Flow<List<LuxuryItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: LuxuryItem)

    @Query("DELETE FROM luxury_items WHERE timestamp < :cutoffTimestamp")
    suspend fun deleteExpiredItems(cutoffTimestamp: Long): Int

    @Query("DELETE FROM luxury_items WHERE id = :id")
    suspend fun deleteById(id: Int)
}
