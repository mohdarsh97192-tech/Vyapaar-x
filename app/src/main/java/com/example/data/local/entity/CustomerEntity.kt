package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customers")
data class CustomerEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val phone: String = "",
    val creditBalance: Double = 0.0, // positive means customer owes money (Udhar)
    val notes: String = "",
    val address: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
