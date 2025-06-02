package com.example.universitybudgetapp.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.universitybudgetapp.viewmodel.EntryViewModel
import com.example.universitybudgetapp.data.model.Category
import com.example.universitybudgetapp.data.model.Entry
import com.example.universitybudgetapp.viewmodel.NotificationViewModel
import java.time.YearMonth

@Composable
fun StatsScreen(
    currentYearMonth: YearMonth,
    notificationViewModel: NotificationViewModel,
    viewModel: EntryViewModel = viewModel()
) {
    val entries by viewModel.entries.collectAsState()
    var selectedTabIndex by remember { mutableStateOf(0) }

    val filteredEntries = entries.filter {
        it.date.year == currentYearMonth.year &&
                it.date.monthValue == currentYearMonth.monthValue
    }

    val incomeEntries = filteredEntries.filter { it.isIncome }
    val expenseEntries = filteredEntries.filter { !it.isIncome }

    val tabTitles = listOf("수입", "지출")

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // 탭
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.fillMaxWidth()
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 그래프바 (수입/지출)
        when (selectedTabIndex) {
            0 -> {
                if (incomeEntries.isNotEmpty()) {
                    StatsBarGraph(incomeEntries)
                }
            }
            1 -> {
                if (expenseEntries.isNotEmpty()) {
                    StatsBarGraph(expenseEntries)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 탭 컨텐츠
        when (selectedTabIndex) {
            0 -> {
                if (incomeEntries.isNotEmpty()) {
                    StatsContent("수입", incomeEntries)
                } else {
                    EmptyContent("수입")
                }
            }
            1 -> {
                if (expenseEntries.isNotEmpty()) {
                    StatsContent("지출", expenseEntries)
                } else {
                    EmptyContent("지출")
                }
            }
        }
    }
}

@Composable
fun StatsBarGraph(entries: List<Entry>) {
    val total = entries.sumOf { it.amount }
    val groupedStats = entries.groupBy { it.category }
        .map { (cat, list) -> cat to list.sumOf { it.amount } }
        .sortedByDescending { it.second }

    if (groupedStats.isEmpty() || total == 0L) return

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp) // 👈 padding 추가
            .height(20.dp)
            .clip(RoundedCornerShape(10.dp))
    ) {
        groupedStats.forEach { (category, amount) ->
            val ratio = amount.toFloat() / total
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(ratio)
                    .background(category.color)
            )
        }
    }
}

@Composable
fun StatsContent(title: String, entries: List<Entry>) {
    val total = entries.sumOf { it.amount }
    val groupedStats = entries.groupBy { it.category }
        .map { (cat, list) -> cat to list.sumOf { it.amount } }
        .sortedByDescending { it.second }

    Column(modifier = Modifier.padding(horizontal = 8.dp)) {
        Text("총 $title: %,d원".format(total), style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        groupedStats.forEach { (category, amount) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = category.icon,
                    contentDescription = category.name,
                    tint = category.color,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = category.name,
                    modifier = Modifier.weight(1f)
                )
                Text("%,d원".format(amount))
            }
            Divider()
        }
    }
}

@Composable
fun EmptyContent(type: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text("데이터가 없습니다.", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
