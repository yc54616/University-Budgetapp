package com.example.universitybudgetapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entries")
data class Entry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amount: Int,
    val description: String,
    val isIncome: Boolean,
    val date: String // ✅ 날짜 추가
)


