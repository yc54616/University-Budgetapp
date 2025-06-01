package com.example.universitybudgetapp.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.universitybudgetapp.viewmodel.EntryViewModel
import com.example.universitybudgetapp.viewmodel.NotificationViewModel
import com.example.universitybudgetapp.data.model.Entry
import com.example.universitybudgetapp.data.model.Category
import com.example.universitybudgetapp.ui.components.DateGroupHeader
import com.example.universitybudgetapp.ui.components.EntryLineItem
import com.example.universitybudgetapp.ui.components.SummaryHeader
import com.example.universitybudgetapp.ui.components.NotificationDialog
import java.time.LocalDate
import java.time.YearMonth
import com.example.universitybudgetapp.data.model.NotificationItem
import com.example.universitybudgetapp.ui.components.NotificationSummaryDialog

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreenContent(
    navController: NavController,
    currentYearMonth: YearMonth,
    entryViewModel: EntryViewModel = viewModel(),
    notificationViewModel: NotificationViewModel
) {
    val entries by entryViewModel.entries.collectAsState()
    val notifications by notificationViewModel.notifications.collectAsState()

    // 🔥 TopBar가 제거되어 별도의 알림 버튼이 없는 상태
    // 만약 이메일 아이콘 클릭 시 showDialog = true로 열고 싶다면 상단에서 버튼을 추가해 주세요.

    val showDialog = notificationViewModel.showDialog

    LaunchedEffect(notifications) {
        if (notifications.isNotEmpty() && !notificationViewModel.alreadyDismissed) {
            notificationViewModel.showDialog()
        }
    }


    val filtered = entries.filter {
        it.date.year == currentYearMonth.year &&
                it.date.monthValue == currentYearMonth.monthValue
    }

    if (filtered.isEmpty()) {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("기록이 없습니다")
        }
    } else {
        val grouped = filtered
            .sortedByDescending { it.date }
            .groupBy { it.date }
        val incomeTotal = filtered.filter { it.isIncome }.sumOf { it.amount }
        val expenseTotal = filtered.filter { !it.isIncome }.sumOf { it.amount }

        var isNavigating by remember { mutableStateOf(false) }

        LazyColumn(
            contentPadding = PaddingValues(
                top = 16.dp,
                bottom = 100.dp,
                start = 16.dp,
                end = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                SummaryHeader(incomeTotal = incomeTotal, expenseTotal = expenseTotal)
            }

            grouped.forEach { (date, dailyEntries) ->
                val dailyIncome = dailyEntries.filter { it.isIncome }.sumOf { it.amount }
                val dailyExpense = dailyEntries.filter { !it.isIncome }.sumOf { it.amount }

                item {
                    DateGroupHeader(
                        date = date.toString(),
                        income = dailyIncome,
                        expense = dailyExpense
                    )
                }

                items(dailyEntries) { entry ->
                    EntryLineItem(
                        entry = entry,
                        onClick = {
                            if (!isNavigating) {
                                isNavigating = true
                                navController.navigate("entry_detail/${entry.id}") {
                                    launchSingleTop = true
                                }
                                // Optionally: Delay나 Effect로 isNavigating 초기화 가능
                            }
                        }
                    )
                }

            }
        }
    }

    if (showDialog) {
        NotificationSummaryDialog(
            count = notifications.size,
            onConfirm = {
                notificationViewModel.hideDialog()
                navController.navigate("notification_list")
            },
            onDismiss = {
                notificationViewModel.hideDialog()
                notificationViewModel.markDialogDismissed()
            }
        )
    }



}
