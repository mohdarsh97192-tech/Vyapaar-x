package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.CustomerDao
import com.example.data.local.dao.ExpenseDao
import com.example.data.local.dao.NotificationDao
import com.example.data.local.dao.ProductDao
import com.example.data.local.dao.SaleDao
import com.example.data.local.dao.UserDao
import com.example.data.local.entity.CustomerEntity
import com.example.data.local.entity.ExpenseEntity
import com.example.data.local.entity.NotificationEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.SaleEntity
import com.example.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        ProductEntity::class,
        CustomerEntity::class,
        SaleEntity::class,
        ExpenseEntity::class,
        NotificationEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class BizNovaDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao
    abstract fun customerDao(): CustomerDao
    abstract fun saleDao(): SaleDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var INSTANCE: BizNovaDatabase? = null

        fun getDatabase(context: Context): BizNovaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BizNovaDatabase::class.java,
                    "biznova_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
