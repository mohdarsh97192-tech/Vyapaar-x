package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.SaleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SaleDao {
    @Query("SELECT * FROM sales ORDER BY timestamp DESC")
    fun getAllSales(): Flow<List<SaleEntity>>

    @Query("SELECT * FROM sales WHERE timestamp >= :startTime ORDER BY timestamp DESC")
    fun getSalesSince(startTime: Long): Flow<List<SaleEntity>>

    @Query("SELECT * FROM sales WHERE customerId = :customerId ORDER BY timestamp DESC")
    fun getSalesForCustomer(customerId: Long): Flow<List<SaleEntity>>

    @Query("SELECT * FROM sales WHERE id = :id LIMIT 1")
    suspend fun getSaleById(id: Long): SaleEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSale(sale: SaleEntity): Long

    @Query("SELECT SUM(netAmount) FROM sales WHERE timestamp >= :startTime")
    fun getRevenueSince(startTime: Long): Flow<Double?>

    @Query("SELECT SUM(estimatedProfit) FROM sales WHERE timestamp >= :startTime")
    fun getProfitSince(startTime: Long): Flow<Double?>

    @Query("SELECT COUNT(*) FROM sales WHERE timestamp >= :startTime")
    fun getSalesCountSince(startTime: Long): Flow<Int>
}
