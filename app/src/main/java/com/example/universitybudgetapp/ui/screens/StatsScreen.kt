package com.example.universitybudgetapp.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.universitybudgetapp.viewmodel.EntryViewModel
import com.example.universitybudgetapp.data.model.Category
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StatsScreen(
    currentYearMonth: YearMonth,
    viewModel: EntryViewModel = viewModel()
) {
    val entries by viewModel.entries.collectAsState()

    // 해당 연·월 데이터 필터링
    val filtered = entries.filter {
        it.date.year == currentYearMonth.year &&
                it.date.monthValue == currentYearMonth.monthValue
    }

    // 수입/지출 구분
    val incomeEntries  = filtered.filter { it.isIncome }
    val expenseEntries = filtered.filter { !it.isIncome }

    // 총합 계산
    val totalIncome  = incomeEntries.sumOf { it.amount }
    val totalExpense = expenseEntries.sumOf { it.amount }

    // 카테고리별 통계
    val incomeStats: List<Pair<Category, Int>> =
        incomeEntries.groupBy { it.category }
            .map { (cat, list) -> cat to list.sumOf { it.amount } }
            .sortedByDescending { it.second }

    val expenseStats: List<Pair<Category, Int>> =
        expenseEntries.groupBy { it.category }
            .map { (cat, list) -> cat to list.sumOf { it.amount } }
            .sortedByDescending { it.second }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 헤더
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    Icons.Default.ShowChart,
                    contentDescription = "통계",
                    modifier = Modifier.size(28.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "${currentYearMonth.year}년 ${currentYearMonth.monthValue}월",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }

        // 수입 섹션
        item {
            Text(
                text = "수입 통계",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "총 수입: %,d원".format(totalIncome),
                style = MaterialTheme.typography.bodyLarge
            )
        }
        items(incomeStats) { (category, amount) ->
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
                Spacer(Modifier.width(8.dp))
                Text(
                    text = category.name,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "%,d원".format(amount),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // Divider between sections
        item {
            Divider(color = MaterialTheme.colorScheme.outline)
        }

        // 지출 섹션
        item {
            Text(
                text = "지출 통계",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "총 지출: %,d원".format(totalExpense),
                style = MaterialTheme.typography.bodyLarge
            )
        }
        items(expenseStats) { (category, amount) ->
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
                Spacer(Modifier.width(8.dp))
                Text(
                    text = category.name,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "%,d원".format(amount),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
