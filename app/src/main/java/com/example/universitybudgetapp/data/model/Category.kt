package com.example.universitybudgetapp.data.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

sealed class Category(
    open val name: String,
    open val icon: ImageVector,
    open val color: Color,
    open val internalId: String, // 🔥 DB 저장용 식별자
    open val type: Type // 🔥 수입/지출 구분
) {
    enum class Type { INCOME, EXPENSE }

    object 식비 : Category("식비", Icons.Default.Restaurant, Color(0xFF42A5F5), "food", Type.EXPENSE)
    object 문화 : Category("문화생활", Icons.Default.Movie, Color(0xFFAB47BC), "culture", Type.EXPENSE)
    object 패션 : Category("패션/미용", Icons.Default.ShoppingBag, Color(0xFFEC407A), "fashion", Type.EXPENSE)
    object 부수입 : Category("부수입", Icons.Default.AttachMoney, Color(0xFF66BB6A), "sideincome", Type.INCOME)
    object 기타 : Category("기타", Icons.Default.Category, Color.Gray, "etc", Type.EXPENSE)

    data class Custom(
        override val name: String,
        override val icon: ImageVector,
        override val color: Color,
        val iconName: String = "Category",
        override val type: Type // 🔥 사용자 정의도 타입 추가
    ) : Category(name, icon, color, "custom:$name", type)

    companion object {
        fun defaultList(): List<Category> = listOf(식비, 문화, 패션, 부수입, 기타)

        fun fromId(id: String): Category {
            return defaultList().find { it.internalId == id } ?: 기타
        }

        // ✅ 아이콘 이름 → 실제 아이콘으로 변환
        fun iconFromName(name: String): ImageVector {
            return when (name) {
                "Restaurant" -> Icons.Default.Restaurant
                "Movie" -> Icons.Default.Movie
                "ShoppingBag" -> Icons.Default.ShoppingBag
                "AttachMoney" -> Icons.Default.AttachMoney
                "Category" -> Icons.Default.Category
                else -> Icons.Default.Category
            }
        }
    }
}
