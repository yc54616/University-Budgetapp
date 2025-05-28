package com.example.universitybudgetapp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun AddEntryButton(navController: NavHostController) {
    FloatingActionButton(onClick = { navController.navigate("add_entry") }) {
        Icon(Icons.Default.Add, contentDescription = "항목 추가")
    }
}

