package com.example.universitybudgetapp.data.db

import androidx.room.*
import com.example.universitybudgetapp.data.model.Entry
import kotlinx.coroutines.flow.Flow

@Dao
interface EntryDao {
    @Insert
    suspend fun insert(entry: Entry)

    @Query("SELECT * FROM entries ORDER BY id DESC")
    fun getAllEntries(): Flow<List<Entry>> // ✅ Flow 반환
}


