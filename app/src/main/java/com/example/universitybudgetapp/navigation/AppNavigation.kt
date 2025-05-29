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
    currentYearMonth: YearMonth
) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreenContent(navController = navController,currentYearMonth  = currentYearMonth)
        }
        composable("stats") { StatsScreen() }
        composable("settings") { SettingsScreen() }
        composable( "add_entry") { AddEntryScreen(navController = navController) }
    }
}


