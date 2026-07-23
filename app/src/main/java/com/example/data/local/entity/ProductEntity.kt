package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val sku: String = "",
    val barcode: String = "",
    val category: String = "General",
    val purchasePrice: Double = 0.0,
    val sellingPrice: Double = 0.0,
    val quantity: Int = 0,
    val lowStockThreshold: Int = 5,
    val unit: String = "pcs",
    val imageUrl: String = "",
    val updatedAt: Long = System.currentTimeMillis()
)
