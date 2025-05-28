package com.example.universitybudgetapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.universitybudgetapp.ui.screens.HomeScreenContent
import com.example.universitybudgetapp.ui.screens.AddEntryScreen
import com.example.universitybudgetapp.ui.screens.SettingsScreen
import com.example.universitybudgetapp.ui.screens.StatsScreen

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreenContent(navController) }
        composable("stats") { StatsScreen() }
        composable("settings") { SettingsScreen() }
        composable( "add_entry") { AddEntryScreen(navController = navController) }
    }
}


