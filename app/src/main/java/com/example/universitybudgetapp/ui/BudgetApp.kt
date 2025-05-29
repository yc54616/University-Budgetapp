package com.example.universitybudgetapp.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.universitybudgetapp.navigation.AppNavigation
import com.example.universitybudgetapp.ui.components.*
import com.example.universitybudgetapp.ui.helpers.currentBackStackEntry
import com.example.universitybudgetapp.ui.screens.HomeScreenContent
import com.example.universitybudgetapp.ui.helpers.currentBackStackEntry

@Composable
fun BudgetApp() {
    var selectedItem by remember { mutableStateOf<BottomNavItem>(BottomNavItem.Home) }
    val navController = rememberNavController()
    val currentRoute = currentBackStackEntry(navController)

    Scaffold(
        topBar = {
            if (currentRoute != "add_entry") {
                BudgetTopBar(currentRoute = currentRoute)
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
                    navController.navigate("add_entry") {
                        launchSingleTop = true
                    }
                }) {
                    Icon(Icons.Default.Add, contentDescription = "항목 추가")
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            AppNavigation(navController)
        }
    }

}


