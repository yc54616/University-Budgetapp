package com.example.universitybudgetapp.ui.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.universitybudgetapp.viewmodel.EntryViewModel
import com.example.universitybudgetapp.viewmodel.NotificationViewModel
import com.example.universitybudgetapp.data.model.Entry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectEntryForRefundScreen(
    navController: NavController,
    notificationItemId: String,
    entryViewModel: EntryViewModel = viewModel(),
    notificationViewModel: NotificationViewModel = viewModel(),
    onRefundProcessed: (Entry, Long) -> Unit
) {
    val entries by entryViewModel.entries.collectAsState()
    var isClicked by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("환급 항목 선택") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(entries) { entry ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (!isClicked) {
                                isClicked = true
                                val notificationItem = notificationViewModel.getNotificationById(notificationItemId)
                                notificationItem?.let { item ->
                                    val refundAmount = item.amount
                                    onRefundProcessed(entry, refundAmount)
                                    navController.popBackStack()
                                }
                            }
                        }
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text("설명: ${entry.description}")
                        Text("금액: ${"%,d원".format(entry.amount)}")
                        Text("날짜: ${entry.date}")
                    }
                }
            }
        }
    }
}