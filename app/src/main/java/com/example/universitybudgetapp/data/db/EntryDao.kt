package com.example.universitybudgetapp.data.db

import androidx.room.*
import com.example.universitybudgetapp.data.model.Entry
import kotlinx.coroutines.flow.Flow

@Dao
interface EntryDao {
    @Insert
    suspend fun insert(entry: Entry)

    @Query("SELECT * FROM entries ORDER BY id DESC")
    fun getAllEntries(): Flow<List<Entry>>

    @Query("DELETE FROM entries")
    suspend fun deleteAll()

    @Update
    suspend fun update(entry: Entry)

    // ✅ Entry 단건 삭제 함수 추가
    @Delete
    suspend fun delete(entry: Entry)
}



