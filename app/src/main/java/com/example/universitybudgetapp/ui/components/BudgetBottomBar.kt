package com.example.universitybudgetapp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val label: String, val icon: ImageVector) {
    object Home : BottomNavItem("home", "가계부", Icons.Outlined.MenuBook)
    object Stats : BottomNavItem("stats", "통계", Icons.Default.BarChart)
    object Settings : BottomNavItem("settings", "설정", Icons.Default.Settings)
}

@Composable
fun BudgetBottomBar(
    selectedItem: BottomNavItem,
    onItemSelected: (BottomNavItem) -> Unit
) {
    NavigationBar {
        listOf(BottomNavItem.Home, BottomNavItem.Stats, BottomNavItem.Settings).forEach { item ->
            NavigationBarItem(
                selected = selectedItem == item,
                onClick = { onItemSelected(item) },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}

