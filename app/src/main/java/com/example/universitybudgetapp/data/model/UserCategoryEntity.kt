package com.example.universitybudgetapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_category")
data class UserCategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val iconName: String,   // ✅ 이 줄 추가
    val color: Long         // ✅ color는 Color.value 저장용
)
