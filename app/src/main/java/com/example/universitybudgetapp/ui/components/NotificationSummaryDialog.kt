package com.example.universitybudgetapp.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun NotificationSummaryDialog(
    count: Int,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("예")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("아니오")
            }
        },
        title = { Text("알림 등록") },
        text = { Text("등록되지 않은 알림 ${count}건이 확인되었습니다. 지금 등록하시겠습니까?") }
    )
}
