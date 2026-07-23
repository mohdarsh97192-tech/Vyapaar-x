package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val message: String,
    val type: String = "DAILY_SUMMARY", // LOW_STOCK, PAYMENT_DUE, DAILY_SUMMARY, AI_TIP
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)
