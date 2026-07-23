package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val category: String = "Other", // Rent, Salary, Electricity, Transport, Inventory, Other
    val amount: Double = 0.0,
    val note: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
