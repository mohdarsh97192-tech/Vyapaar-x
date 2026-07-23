package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String = "default_user",
    val name: String = "Rajesh Sharma",
    val phone: String = "+91 98765 43210",
    val email: String = "rajesh.sharma@biznova.in",
    val businessType: String = "Kirana & General Store", // Kirana, Medical, Hardware, Mobile, Garment, Restaurant, Manufacturer
    val businessName: String = "Sharma Kirana & General Store",
    val gstNumber: String = "07AAAAA0000A1Z5",
    val address: String = "Shop No 12, Main Market, Sector 15, New Delhi",
    val avatarUrl: String = "",
    val isLoggedIn: Boolean = true,
    val preferredLanguage: String = "en" // "en" or "hi"
)
