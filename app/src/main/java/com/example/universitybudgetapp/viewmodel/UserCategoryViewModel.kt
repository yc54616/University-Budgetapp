package com.example.universitybudgetapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.universitybudgetapp.data.db.UserCategoryDao
import com.example.universitybudgetapp.data.model.UserCategoryEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserCategoryViewModel(
    private val dao: UserCategoryDao
) : ViewModel() {

    val userCategories = dao.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addCategory(name: String, iconName: String, type: String) { // 🔥 타입 추가
        val color = when (iconName) {
            "Restaurant" -> 0xFF42A5F5L
            "Movie" -> 0xFFAB47BCL
            "ShoppingBag" -> 0xFFEC407AL
            "AttachMoney" -> 0xFF66BB6AL
            else -> 0xFF888888L
        }

        viewModelScope.launch {
            dao.insert(
                UserCategoryEntity(
                    name = name,
                    iconName = iconName,
                    color = color,
                    type = type // 🔥 DB에 저장
                )
            )
        }
    }
}

