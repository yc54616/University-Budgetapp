package com.example.universitybudgetapp.data.db

import androidx.room.*
import com.example.universitybudgetapp.data.model.UserCategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserCategoryDao {
    @Insert
    suspend fun insert(category: UserCategoryEntity)

    @Query("SELECT * FROM user_category")
    fun getAll(): Flow<List<UserCategoryEntity>>
}

