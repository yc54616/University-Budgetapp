package com.example.universitybudgetapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.universitybudgetapp.viewmodel.EntryViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.universitybudgetapp.ui.components.EntryRow

@Composable
fun HomeScreenContent(
    navController: NavController,
    viewModel: EntryViewModel = viewModel()
) {
    val entries by viewModel.entries.collectAsState()

    if (entries.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("기록이 없습니다", style = MaterialTheme.typography.bodyMedium)
        }
    } else {
        val grouped = entries
            .sortedByDescending { it.date }
            .groupBy { it.date }

        LazyColumn(
            contentPadding = PaddingValues(16.dp)
        ) {
            grouped.forEach { (date, dailyEntries) ->
                item {
                    Text(
                        text = date,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                items(dailyEntries) { entry ->
                    EntryRow(entry = entry)  // ✅ 카드 대신 Row
                }
            }
        }

    }
}





