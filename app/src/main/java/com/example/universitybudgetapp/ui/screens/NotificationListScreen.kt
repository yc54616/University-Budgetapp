package com.example.universitybudgetapp.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.universitybudgetapp.viewmodel.EntryViewModel
import com.example.universitybudgetapp.viewmodel.NotificationViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationListScreen(
    navController: NavController,
    notificationViewModel: NotificationViewModel = viewModel(),
    entryViewModel: EntryViewModel = viewModel()
) {
    val notifications by notificationViewModel.notifications.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val recentlyRefundedEntry by notificationViewModel.recentlyRefundedEntry.collectAsState()
    val recentlyRefundedRefundAmount by notificationViewModel.recentlyRefundedRefundAmount.collectAsState()
    val recentlyRefundedNotification by notificationViewModel.recentlyRefundedNotification.collectAsState()

    LaunchedEffect(recentlyRefundedEntry) {
        recentlyRefundedEntry?.let { entry ->

            val refundAmount = recentlyRefundedRefundAmount ?: 0L

            val result = snackbarHostState.showSnackbar(
                message = "${entry.description} 환급 완료",
                actionLabel = "되돌리기",
                duration = SnackbarDuration.Short
            )
            if (result == SnackbarResult.ActionPerformed) {
                entryViewModel.getEntryByIdFromDb(entry.id) { updatedEntry ->
                    updatedEntry?.let { safeEntry ->
                        entryViewModel.undoRefund(safeEntry, refundAmount)
                    }
                }
                recentlyRefundedNotification?.let {
                    notificationViewModel.addNotification(it)
                }
            }

            notificationViewModel.clearRecentlyRefunded()
        }
    }





    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("알림 내역") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        if (notifications.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("새로운 알림이 없습니다.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(notifications) { item ->
                    Card(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(12.dp)) {
                            Text("제목: ${item.title}")
                            Text("내용: ${item.description}")
                            Text("금액: ${"%,d원".format(item.amount)}")
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedButton(onClick = {
                                    // ✅ 추가하기
                                    val isIncome = item.type == "수입"
                                    val category = if (isIncome) com.example.universitybudgetapp.data.model.Category.부수입 else com.example.universitybudgetapp.data.model.Category.기타지출
                                    entryViewModel.insertEntry(
                                        com.example.universitybudgetapp.data.model.Entry(
                                            amount = item.amount,
                                            description = item.description,
                                            isIncome = isIncome,
                                            date = java.time.LocalDate.now(),
                                            category = category
                                        )
                                    )
                                    notificationViewModel.removeNotification(item)
                                }) {
                                    Text("추가")
                                }

                                OutlinedButton(onClick = {
                                    navController.navigate("select_entry_for_refund/${item.id}")
                                }) {
                                    Text("환급하기")
                                }

                                OutlinedButton(onClick = {
                                    notificationViewModel.removeNotification(item)
                                    coroutineScope.launch {
                                        val result = snackbarHostState.showSnackbar(
                                            message = "알림이 삭제됨",
                                            actionLabel = "되돌리기"
                                        )
                                        if (result == SnackbarResult.ActionPerformed) {
                                            notificationViewModel.addNotification(item)
                                        }
                                    }
                                }) {
                                    Text("삭제")
                                }
                            }

                        }
                    }
                }
            }
        }
    }
}