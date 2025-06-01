package com.example.universitybudgetapp.ui.screens

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
import com.example.universitybudgetapp.data.model.Entry
import com.example.universitybudgetapp.data.model.Category
import com.example.universitybudgetapp.viewmodel.EntryViewModel
import com.example.universitybudgetapp.viewmodel.NotificationViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationListScreen(
    navController: NavController,
    notificationViewModel: NotificationViewModel = viewModel(),
    entryViewModel: EntryViewModel = viewModel()
) {
    val notifications by notificationViewModel.notifications.collectAsState()

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
        }
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
                                    val isIncome = item.type == "수입"
                                    val category = if (isIncome) Category.부수입 else Category.기타지출
                                    entryViewModel.insertEntry(
                                        Entry(
                                            amount = item.amount,
                                            description = item.description,
                                            isIncome = isIncome,
                                            date = LocalDate.now(),
                                            category = category
                                        )
                                    )
                                    notificationViewModel.removeNotification(item)
                                }) {
                                    Text("추가")
                                }
                                OutlinedButton(onClick = {
                                    notificationViewModel.removeNotification(item)
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
