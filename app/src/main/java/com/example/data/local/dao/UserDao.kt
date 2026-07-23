package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    fun getUserFlow(id: String = "default_user"): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUser(id: String = "default_user"): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUser(user: UserEntity)
}
