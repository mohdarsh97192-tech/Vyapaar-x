package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sales")
data class SaleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val invoiceNumber: String,
    val customerId: Long? = null,
    val customerName: String = "Walk-in Customer",
    val customerPhone: String = "",
    val totalAmount: Double = 0.0,
    val discountAmount: Double = 0.0,
    val gstAmount: Double = 0.0,
    val netAmount: Double = 0.0,
    val estimatedProfit: Double = 0.0,
    val paymentMethod: String = "Cash", // Cash, UPI, Card, Udhar
    val paymentStatus: String = "Paid", // Paid, Pending
    val itemsSummary: String = "", // JSON string or human string representation of items
    val timestamp: Long = System.currentTimeMillis()
)
