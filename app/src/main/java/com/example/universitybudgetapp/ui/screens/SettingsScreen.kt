package com.example.universitybudgetapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.universitybudgetapp.viewmodel.EntryViewModel

@Composable
fun SettingsScreen(
    viewModel: EntryViewModel = viewModel(),
    onThemeChange: (Boolean) -> Unit = {},   // 다크모드 토글 콜백 (예시)
    isDarkTheme: Boolean = false             // 다크모드 상태 (예시)
) {
    var currency by remember { mutableStateOf("₩ KRW") }
    val appVersion = "1.0.0"  // 실제 앱 버전은 BuildConfig.VERSION_NAME 등에서 가져오세요

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text("⚙️ 설정", style = MaterialTheme.typography.headlineSmall)


        // 다크 모드 토글
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("다크 모드", modifier = Modifier.weight(1f))
            Switch(
                checked = isDarkTheme,
                onCheckedChange = { onThemeChange(it) }
            )
        }

// 데이터 초기화 버튼
        Button(
            onClick = {
                viewModel.deleteAllEntries()
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("데이터 초기화", color = MaterialTheme.colorScheme.onError)
        }

        Spacer(modifier = Modifier.weight(1f)) // 남은 공간 채우기

        // 버전 정보
        Text(
            "앱 버전: $appVersion",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}
