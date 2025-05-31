package com.example.universitybudgetapp.ui.components

import AddCategoryDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.universitybudgetapp.data.model.Category
import com.example.universitybudgetapp.data.model.Category.Companion.iconFromName
import com.example.universitybudgetapp.viewmodel.UserCategoryViewModel

@Composable
fun CategorySelector(
    selected: Category,
    selectedType: Category.Type, // 🔥 수입/지출 타입 전달
    onSelected: (Category) -> Unit,
    userCategoryViewModel: UserCategoryViewModel = viewModel()
) {
    var expanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    val userCategories by userCategoryViewModel.userCategories.collectAsState()

    Column {
        OutlinedButton(onClick = { expanded = true }) {
            Icon(selected.icon, contentDescription = selected.name, tint = selected.color)
            Spacer(Modifier.width(8.dp))
            Text(selected.name)
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            // 🔥 기본 카테고리 타입 필터링
            Category.defaultList()
                .filter { it.type == selectedType }
                .forEach {
                    DropdownMenuItem(
                        text = { Text(it.name) },
                        leadingIcon = {
                            Icon(it.icon, contentDescription = it.name, tint = it.color)
                        },
                        onClick = {
                            onSelected(it)
                            expanded = false
                        }
                    )
                }

            // 🔥 사용자 카테고리 타입 필터링
            val filteredUserCategories = userCategories.filter { it.type == selectedType.name }
            if (filteredUserCategories.isNotEmpty()) {
                Divider()
                filteredUserCategories.forEach { entity ->
                    val icon = iconFromName(entity.iconName)
                    val color = Color(entity.color)
                    val customCategory = Category.Custom(
                        name = entity.name,
                        icon = icon,
                        color = color,
                        iconName = entity.iconName,
                        type = selectedType // 🔥 타입 정보 전달
                    )
                    DropdownMenuItem(
                        text = { Text(customCategory.name) },
                        leadingIcon = {
                            Icon(customCategory.icon, contentDescription = customCategory.name, tint = customCategory.color)
                        },
                        onClick = {
                            onSelected(customCategory)
                            expanded = false
                        }
                    )
                }
            }

            Divider()
            DropdownMenuItem(
                text = { Text("\u2795 새 카테고리 추가") },
                onClick = {
                    expanded = false
                    showDialog = true
                }
            )
        }
    }

    if (showDialog) {
        AddCategoryDialog(
            onDismiss = { showDialog = false },
            onConfirm = { name, iconName ->
                userCategoryViewModel.addCategory(name, iconName, selectedType.name) // 🔥 타입 전달
                val color = Color(
                    when (iconName) {
                        "Restaurant" -> 0xFF42A5F5L
                        "Movie" -> 0xFFAB47BCL
                        "ShoppingBag" -> 0xFFEC407AL
                        "AttachMoney" -> 0xFF66BB6AL
                        else -> 0xFF888888L
                    }
                )
                onSelected(
                    Category.Custom(
                        name = name,
                        icon = Category.iconFromName(iconName),
                        color = color,
                        iconName = iconName,
                        type = selectedType // 🔥 타입 전달
                    )
                )
                showDialog = false
            }
        )
    }
}

