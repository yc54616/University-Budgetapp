package com.example.universitybudgetapp.data.model

import androidx.compose.ui.graphics.Color
import androidx.room.TypeConverter

class CategoryConverter {

    @TypeConverter
    fun fromCategory(category: Category): String {
        return when (category) {
            is Category.Custom ->
                // 🔥 타입 정보 추가 (마지막에 type.name)
                "custom:${category.name}:${category.iconName}:${category.color.value.toLong()}:${category.type.name}"
            else -> category.internalId // 기본 카테고리는 internalId로 처리
        }
    }

    @TypeConverter
    fun toCategory(serial: String): Category {
        return when {
            serial.startsWith("custom:") -> {
                val parts = serial.removePrefix("custom:").split(":")
                val name = parts.getOrNull(0) ?: "사용자"
                val iconName = parts.getOrNull(1) ?: "Category"
                val colorLong = parts.getOrNull(2)?.toLongOrNull() ?: Color.Gray.value.toLong()
                val typeName = parts.getOrNull(3) ?: Category.Type.EXPENSE.name // 기본은 EXPENSE

                val icon = Category.iconFromName(iconName)
                val color = Color(colorLong)
                val type = Category.Type.valueOf(typeName)

                Category.Custom(
                    name = name,
                    icon = icon,
                    color = color,
                    iconName = iconName,
                    type = type
                )
            }
            else -> Category.fromId(serial)
        }
    }
}
