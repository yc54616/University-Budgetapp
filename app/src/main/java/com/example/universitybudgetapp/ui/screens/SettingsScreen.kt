// com/example/universitybudgetapp/ui/screens/SettingsScreen.kt

package com.example.universitybudgetapp.ui.screens

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.material3.Switch
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.universitybudgetapp.viewmodel.UserBankAppViewModel
import com.example.universitybudgetapp.viewmodel.UserBankAppViewModelFactory
import com.example.universitybudgetapp.viewmodel.EntryViewModel
import com.example.universitybudgetapp.viewmodel.NotificationViewModel

@Composable
fun SettingsScreen(
    notificationViewModel: NotificationViewModel,
    viewModel: EntryViewModel = viewModel(),
    onThemeChange: (Boolean) -> Unit = {},
    isDarkTheme: Boolean = false
) {
    // ▼ Composable 스코프 안에서 Context와 PackageManager 꺼내기
    val context = LocalContext.current.applicationContext as android.app.Application
    val pm = LocalContext.current.packageManager

    // ▼ UserBankAppViewModel 초기화 (pm 사용은 아니지만, ViewModel 자체는 기존 코드와 동일)
    val bankViewModel: UserBankAppViewModel = viewModel(
        factory = UserBankAppViewModelFactory(context)
    )
    val bankUiState by bankViewModel.uiState.collectAsState()
    val allInstalledApps by bankViewModel.installedApps.collectAsState()

    // ▼ 다이얼로그 표시 여부를 관리할 상태
    var showAddDialog by remember { mutableStateOf(false) }

    // … 나머지 SettingsScreen UI 코드는 다음 단계에서 이어집니다 …

    var currency by remember { mutableStateOf("₩ KRW") }
    val appVersion = "1.0.0"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // ─────────────────────────────────────────────────
        // 상단: 타이틀, 다크 모드, 데이터 초기화
        // ─────────────────────────────────────────────────
        Text(
            text = "⚙️ 설정",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "다크 모드",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = isDarkTheme,
                onCheckedChange = { onThemeChange(it) }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.deleteAllEntries() },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "데이터 초기화",
                color = MaterialTheme.colorScheme.onError
            )
        }

        Spacer(modifier = Modifier.height(32.dp))


        // ─────────────────────────────────────────────────
        // 은행/금융 앱 알림 설정 타이틀 + “+” 버튼
        // ─────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "은행/금융 앱 알림 설정",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { showAddDialog = true }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "앱 추가")
            }
        }
        Divider()

        // 은행/금융 앱 토글 리스트
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            // ① bankUiState 중에서 isSelected == true 인 항목만 필터링
            val activeApps = bankUiState.filter { it.isSelected }

            items(
                items = activeApps,
                key = { it.packageName }
            ) { appInfo ->
                BankAppToggleItem(
                    packageName = appInfo.packageName,
                    appLabel = appInfo.appLabel,
                    icon = appInfo.icon,
                    isSelected = appInfo.isSelected,
                    onToggled = { nowChecked ->
                        // 여기 토글을 끄면 isSelected가 false가 되고,
                        // 이후 컴포즈 리컴포지션에 의해 activeApps에서 제외되어 사라집니다
                        bankViewModel.onAppToggled(appInfo, nowChecked)
                    }
                )
                Divider(modifier = Modifier.padding(vertical = 4.dp))
            }
        }

        // 하단: 앱 버전
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "앱 버전: $appVersion",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            textAlign = TextAlign.Center
        )
    }

    // ——————————————————————————————————————————————
    // 다이얼로그: allInstalledApps(필터링된 “금융 앱 전체”)가 표시된다
    // ——————————————————————————————————————————————
    if (showAddDialog) {
        AddBankAppDialog(
            allApps = allInstalledApps,
            alreadySelected = bankUiState
                .filter { it.isSelected }
                .map { it.packageName }
                .toSet(),
            packageManager = pm,
            onDismiss = { showAddDialog = false },
            onAppSelected = { appInfo ->
                val label = pm.getApplicationLabel(appInfo).toString()
                val iconDrawable = pm.getApplicationIcon(appInfo)
                bankViewModel.onAppToggled(
                    UserBankAppViewModel.SelectableAppInfo(
                        packageName = appInfo.packageName,
                        appLabel = label,
                        icon = iconDrawable,
                        isSelected = false
                    ),
                    true
                )
                showAddDialog = false
            }
        )
    }
}

/**
 * “앱 추가” 다이얼로그.
 *  - allApps: 설치된 모든 런처 앱(ApplicationInfo) 목록
 *  - alreadySelected: 이미 선택되어 토글 리스트(bankUiState)에 진입된 패키지명들(중복 방지)
 *  - onDismiss: 다이얼로그를 닫을 때 호출
 *  - onAppSelected: 사용자가 다이얼로그 목록 중에서 앱을 하나 클릭했을 때 호출
 */
@Composable
fun AddBankAppDialog(
    allApps: List<android.content.pm.ApplicationInfo>,
    alreadySelected: Set<String>,
    packageManager: android.content.pm.PackageManager,   // ← ① PackageManager 파라미터 추가
    onDismiss: () -> Unit,
    onAppSelected: (android.content.pm.ApplicationInfo) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "추가할 앱 선택", style = MaterialTheme.typography.titleMedium)
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "목록에서 추가할 앱을 선택하세요.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn {
                    // 이미 선택된 패키지는 목록에서 제외
                    items(items = allApps.filter { !alreadySelected.contains(it.packageName) }) { appInfo ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onAppSelected(appInfo) }  // 클릭 시 콜백
                                .padding(vertical = 8.dp, horizontal = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // ② 여기서도 LocalContext.current 대신 전달받은 packageManager 사용
                            val iconDrawable = appInfo.loadIcon(packageManager)
                            val imageBitmap = remember(iconDrawable) {
                                (iconDrawable as? BitmapDrawable)?.bitmap
                                    ?.asImageBitmap()
                                    ?: Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
                                        .asImageBitmap()
                            }

                            Image(
                                bitmap = imageBitmap,
                                contentDescription = "앱 아이콘",
                                modifier = Modifier.size(32.dp)
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = packageManager.getApplicationLabel(appInfo).toString(),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = appInfo.packageName,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Divider()
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "닫기")
            }
        }
    )
}

/**
 * 은행/금융 앱 토글 아이템 컴포저블 (기존 코드 재사용)
 */
@Composable
fun BankAppToggleItem(
    packageName: String,
    appLabel: String,
    icon: android.graphics.drawable.Drawable,
    isSelected: Boolean,
    onToggled: (Boolean) -> Unit
) {
    val imageBitmap = remember(icon) {
        (icon as? BitmapDrawable)?.bitmap?.asImageBitmap()
            ?: Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888).asImageBitmap()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            bitmap = imageBitmap,
            contentDescription = "앱 아이콘",
            modifier = Modifier.size(36.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = appLabel, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = packageName,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = isSelected,
            onCheckedChange = onToggled
        )
    }
}
