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
import com.example.universitybudgetapp.ui.components.DateGroupHeader
import com.example.universitybudgetapp.ui.components.EntryLineItem
import com.example.universitybudgetapp.ui.components.SummaryHeader

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
        val grouped = entries.sortedByDescending { it.date }.groupBy { it.date }
        val incomeTotal = entries.filter { it.isIncome }.sumOf { it.amount }
        val expenseTotal = entries.filter { !it.isIncome }.sumOf { it.amount }

        LazyColumn(
            contentPadding = PaddingValues(
                top = 16.dp,
                bottom = 100.dp, // ✅ FAB 높이만큼 넉넉하게 padding 줘야 함!
                start = 16.dp,
                end = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ✅ 상단 요약 추가
            item {
                SummaryHeader(incomeTotal = incomeTotal, expenseTotal = expenseTotal)
            }

            // 날짜별 항목
            grouped.forEach { (date, dailyEntries) ->
                val dailyIncome = dailyEntries.filter { it.isIncome }.sumOf { it.amount }
                val dailyExpense = dailyEntries.filter { !it.isIncome }.sumOf { it.amount }

                item {
                    DateGroupHeader(date = date, income = dailyIncome, expense = dailyExpense)
                }

                items(dailyEntries) { entry ->
                    EntryLineItem(entry)
                }
            }
        }
    }
}





