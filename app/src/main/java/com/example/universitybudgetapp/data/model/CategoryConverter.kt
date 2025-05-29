package com.example.universitybudgetapp.data.model

import androidx.compose.ui.graphics.Color
import androidx.room.TypeConverter

class CategoryConverter {

    @TypeConverter
    fun fromCategory(category: Category): String {
        return when (category) {
            is Category.Custom ->
                "custom:${category.name}:${category.iconName}:${category.color.value.toLong()}"
            else -> category.internalId
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
                val icon = Category.iconFromName(iconName)
                val color = Color(colorLong)
                Category.Custom(name = name, icon = icon, color = color, iconName = iconName)
            }
            else -> Category.fromId(serial)
        }
    }
}



