package com.example.universitybudgetapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.universitybudgetapp.ui.screens.HomeScreenContent
import com.example.universitybudgetapp.ui.screens.AddEntryScreen
import com.example.universitybudgetapp.ui.screens.NotificationListScreen
import com.example.universitybudgetapp.ui.screens.SettingsScreen
import com.example.universitybudgetapp.ui.screens.StatsScreen
import com.example.universitybudgetapp.viewmodel.NotificationViewModel
import java.time.YearMonth

@Composable
fun AppNavigation(
    navController: NavHostController,
    currentYearMonth: YearMonth,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    notificationViewModel: NotificationViewModel
) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreenContent(
                navController = navController,
                currentYearMonth = currentYearMonth,
                notificationViewModel = notificationViewModel
            )
        }
        composable("stats") {
            StatsScreen(
                currentYearMonth = currentYearMonth,
                notificationViewModel = notificationViewModel
            )
        }
        composable("settings") {
            SettingsScreen(
                isDarkTheme = isDarkTheme,
                onThemeChange = onThemeChange,
                notificationViewModel = notificationViewModel
            )
        }
        composable("add_entry") {
            AddEntryScreen(navController = navController)
        }
        composable("notification_list") {
            NotificationListScreen(
                navController = navController,
                notificationViewModel = notificationViewModel
            )
        }
    }
}




