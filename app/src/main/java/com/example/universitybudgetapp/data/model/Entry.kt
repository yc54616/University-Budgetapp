package com.example.universitybudgetapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "entries")
data class Entry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amount: Int,
    val description: String,
    val isIncome: Boolean,
    val date: LocalDate,        // ✅ String → LocalDate
    val category: Category      // ✅ CategoryConverter 이미 등록되어 있어야 함
)
