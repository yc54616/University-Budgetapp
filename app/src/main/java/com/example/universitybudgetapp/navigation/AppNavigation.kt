package com.example.universitybudgetapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.universitybudgetapp.ui.screens.HomeScreenContent
import com.example.universitybudgetapp.ui.screens.AddEntryScreen
import com.example.universitybudgetapp.ui.screens.SettingsScreen
import com.example.universitybudgetapp.ui.screens.StatsScreen
import java.time.YearMonth

@Composable
fun AppNavigation(
    navController: NavHostController,
    currentYearMonth: YearMonth,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreenContent(
                navController      = navController,
                currentYearMonth   = currentYearMonth   // ← 넘겨주기
            )
        }
        composable("stats") {
            StatsScreen(
                currentYearMonth   = currentYearMonth   // ← 넘겨주기
            )
        }
        composable("settings") {
            SettingsScreen(
                isDarkTheme = isDarkTheme,
                onThemeChange = onThemeChange
            )
        }
        composable( "add_entry") { AddEntryScreen(navController = navController) }
    }
}


