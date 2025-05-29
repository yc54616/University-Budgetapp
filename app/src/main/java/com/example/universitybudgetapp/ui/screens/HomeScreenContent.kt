package com.example.universitybudgetapp.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.universitybudgetapp.viewmodel.EntryViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.universitybudgetapp.ui.components.DateGroupHeader
import com.example.universitybudgetapp.ui.components.EntryLineItem
import com.example.universitybudgetapp.ui.components.SummaryHeader
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreenContent(
    navController: NavController,
    currentYearMonth: YearMonth,
    viewModel: EntryViewModel = viewModel()
) {
    val entries by viewModel.entries.collectAsState()

    // 🔥 여기를 필터링
    val filtered = entries.filter {
        it.date.year == currentYearMonth.year &&
                it.date.monthValue == currentYearMonth.monthValue
    }

    if (filtered.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("기록이 없습니다")
        }
    } else {
        // 기존대로 grouped, incomeTotal, expenseTotal 계산하되
        // 항상 'filtered'를 쓰면 됩니다
        val grouped = filtered
            .sortedByDescending { it.date }
            .groupBy { it.date }
        val incomeTotal  = filtered.filter  { it.isIncome  }.sumOf { it.amount }
        val expenseTotal = filtered.filter  { !it.isIncome }.sumOf { it.amount }

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
                    DateGroupHeader(date = date.toString(), income = dailyIncome, expense = dailyExpense)
                }

                items(dailyEntries) { entry ->
                    EntryLineItem(entry)
                }
            }
        }
    }
}





