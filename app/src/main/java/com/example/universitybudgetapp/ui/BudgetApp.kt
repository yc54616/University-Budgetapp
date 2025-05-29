package com.example.universitybudgetapp.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.universitybudgetapp.navigation.AppNavigation
import com.example.universitybudgetapp.ui.components.BottomNavItem
import com.example.universitybudgetapp.ui.components.BudgetBottomBar
import com.example.universitybudgetapp.ui.components.BudgetTopBar
import com.example.universitybudgetapp.ui.components.MonthPickerDialog
import com.example.universitybudgetapp.ui.helpers.currentBackStackEntry
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetApp() {
    val navController = rememberNavController()
    var selectedItem: BottomNavItem by remember { mutableStateOf(BottomNavItem.Home) }

    // 현재 연·월 상태 관리
    var currentYearMonth by remember { mutableStateOf(YearMonth.now()) }
    val monthFormatter = remember { DateTimeFormatter.ofPattern("yyyy년 M월") }
    var showMonthPicker by remember { mutableStateOf(false) }

    val currentRoute = currentBackStackEntry(navController)

    Scaffold(
        topBar = {
            if (currentRoute != "add_entry") {
                BudgetTopBar(
                    title         = currentYearMonth.format(monthFormatter),
                    onPrevClick   = { currentYearMonth = currentYearMonth.minusMonths(1) },
                    onNextClick   = { currentYearMonth = currentYearMonth.plusMonths(1) },
                    onTitleClick  = { showMonthPicker = true },
                    onEmailClick  = { /* TODO */ },
                    showBadge     = (currentRoute == "home"),
                    showEmailIcon = (currentRoute == "home")  // 홈일 때만 이메일 아이콘 보이도록
                )
            }
        },
        bottomBar = {
            BudgetBottomBar(
                selectedItem = selectedItem,
                onItemSelected = {
                    selectedItem = it
                    navController.navigate(it.route) {
                        launchSingleTop = true
                        popUpTo("home")
                    }
                }
            )
        },
        floatingActionButton = {
            if (currentRoute == "home") {
                FloatingActionButton(onClick = {
                    navController.navigate("add_entry") { launchSingleTop = true }
                }) {
                    Icon(Icons.Default.Add, contentDescription = "항목 추가")
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            AppNavigation(navController, currentYearMonth)
        }
    }

    // 월 선택 다이얼로그
    if (showMonthPicker) {
        MonthPickerDialog(
            initialYearMonth = currentYearMonth,
            onDismissRequest = { showMonthPicker = false },
            onMonthSelected = {
                currentYearMonth = it
                showMonthPicker = false
            }
        )
    }
}
