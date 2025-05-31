package com.example.universitybudgetapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_category")
data class UserCategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val iconName: String,
    val color: Long,
    val type: String // 🔥 추가: 수입/지출 타입 (Category.Type.name)
)
