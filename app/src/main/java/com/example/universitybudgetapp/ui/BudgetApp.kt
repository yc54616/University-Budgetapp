package com.example.universitybudgetapp.ui

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.text.TextUtils
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.universitybudgetapp.navigation.AppNavigation
import com.example.universitybudgetapp.ui.components.BottomNavItem
import com.example.universitybudgetapp.ui.components.BudgetBottomBar
import com.example.universitybudgetapp.ui.components.BudgetTopBar
import com.example.universitybudgetapp.ui.components.MonthPickerDialog
import com.example.universitybudgetapp.ui.helpers.currentBackStackEntry
import com.example.universitybudgetapp.viewmodel.NotificationViewModel
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetApp(
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    notificationViewModel: NotificationViewModel  // 🔥 추가
) {
    val navController = rememberNavController()
    var selectedItem: BottomNavItem by remember { mutableStateOf(BottomNavItem.Home) }
    var currentYearMonth by remember { mutableStateOf(YearMonth.now()) }
    val monthFormatter = remember { DateTimeFormatter.ofPattern("yyyy년 M월") }
    var showMonthPicker by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // 🔥 알림 리스트 가져오기
    val notifications by notificationViewModel.notifications.collectAsState()

    // 🔥 알림 접근 권한 체크
    LaunchedEffect(Unit) {
        if (!isNotificationServiceEnabled(context)) {
            AlertDialog.Builder(context)
                .setTitle("알림 접근 권한")
                .setMessage("앱에서 알림을 감지하려면 알림 접근 권한이 필요합니다. 설정 화면으로 이동하시겠습니까?")
                .setPositiveButton("이동") { _, _ ->
                    val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
                    context.startActivity(intent)
                }
                .setNegativeButton("취소", null)
                .show()
        }
    }

    val currentRoute = currentBackStackEntry(navController)

    Scaffold(
        topBar = {
            when (currentRoute) {
                "home" -> BudgetTopBar(
                    title = currentYearMonth.format(monthFormatter),
                    onPrevClick = { currentYearMonth = currentYearMonth.minusMonths(1) },
                    onNextClick = { currentYearMonth = currentYearMonth.plusMonths(1) },
                    onTitleClick = { showMonthPicker = true },
                    onEmailClick = { navController.navigate("notification_list") },
                    showBadge = notificationViewModel.notifications.value.isNotEmpty(),
                    showEmailIcon = true
                )
                "stats" -> BudgetTopBar(
                    title = "${currentYearMonth.year}년 ${currentYearMonth.monthValue}월 통계",
                    onPrevClick = { currentYearMonth = currentYearMonth.minusMonths(1) },
                    onNextClick = { currentYearMonth = currentYearMonth.plusMonths(1) },
                    onTitleClick = { showMonthPicker = true },
                    onEmailClick = { /* 필요시 추가 */ },
                    showBadge = false,
                    showEmailIcon = false
                )
                "notification_list" -> {}
                else -> {}
            }
        },
        bottomBar = {
            BudgetBottomBar(
                selectedItem = selectedItem,
                onItemSelected = {
                    selectedItem = it
                    navController.navigate(it.route) {
                        launchSingleTop = true
                        popUpTo("home")
                    }
                }
            )
        },
        floatingActionButton = {
            if (currentRoute == "home") {
                FloatingActionButton(onClick = {
                    navController.navigate("add_entry") { launchSingleTop = true }
                }) {
                    Icon(Icons.Default.Add, contentDescription = "항목 추가")
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            AppNavigation(
                navController = navController,
                currentYearMonth = currentYearMonth,
                isDarkTheme = isDarkTheme,
                onThemeChange = onThemeChange,
                notificationViewModel = notificationViewModel  // 🔥 전달
            )
        }
    }

    if (showMonthPicker) {
        MonthPickerDialog(
            initialYearMonth = currentYearMonth,
            onDismissRequest = { showMonthPicker = false },
            onMonthSelected = {
                currentYearMonth = it
                showMonthPicker = false
            }
        )
    }
}


// 🔥 알림 접근 권한 체크 함수
fun isNotificationServiceEnabled(context: Context): Boolean {
    val pkgName = context.packageName
    val enabledListeners = Settings.Secure.getString(
        context.contentResolver,
        "enabled_notification_listeners"
    )
    return !TextUtils.isEmpty(enabledListeners) && enabledListeners.contains(pkgName)
}
