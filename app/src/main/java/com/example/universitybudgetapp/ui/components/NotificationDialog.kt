package com.example.universitybudgetapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.universitybudgetapp.data.model.NotificationItem

@Composable
fun NotificationDialog(
    notifications: List<NotificationItem>,
    onAdd: (NotificationItem) -> Unit,
    onDelete: (NotificationItem) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("알림 내역") },
        text = {
            Column {
                notifications.forEach { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors()
                    ) {
                        Column(Modifier.padding(8.dp)) {
                            Text("제목: ${item.title}")
                            Text("내용: ${item.description}")
                            Text("금액: ${item.amount}원")
                            Spacer(Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(onClick = { onAdd(item) }) {
                                    Text("추가")
                                }
                                OutlinedButton(onClick = { onDelete(item) }) {
                                    Text("삭제")
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("닫기") }
        }
    )
}


