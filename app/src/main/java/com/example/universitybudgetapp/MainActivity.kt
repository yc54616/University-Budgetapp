package com.example.universitybudgetapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.universitybudgetapp.data.db.AppDatabase
import com.example.universitybudgetapp.data.model.NotificationItem
import com.example.universitybudgetapp.data.repository.NotificationRepository
import com.example.universitybudgetapp.ui.BudgetApp
import com.example.universitybudgetapp.ui.theme.UniversityBudgetAppTheme
import com.example.universitybudgetapp.viewmodel.NotificationViewModel
import com.example.universitybudgetapp.viewmodel.NotificationViewModelFactory
import com.example.universitybudgetapp.viewmodel.ThemeViewModel

class MainActivity : ComponentActivity() {

    private lateinit var notificationViewModel: NotificationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // DB와 Repository 설정
        val database = AppDatabase.getInstance(applicationContext)
        val repository = NotificationRepository(database.notificationDao())
        val factory = NotificationViewModelFactory(repository)
        notificationViewModel = ViewModelProvider(this, factory)[NotificationViewModel::class.java]

        // BroadcastReceiver 등록
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                intent?.let {
                    val item = NotificationItem(
                        title = it.getStringExtra("title") ?: "",
                        description = it.getStringExtra("description") ?: "",
                        amount = it.getLongExtra("amount", 0L),
                        type = it.getStringExtra("type") ?: "기타"
                    )
                    notificationViewModel.addNotification(item)
                }
            }
        }
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(receiver, IntentFilter("BANK_NOTIFICATION_EVENT"))

        // Compose Content
        setContent {
            val themeViewModel: ThemeViewModel = ViewModelProvider(this)[ThemeViewModel::class.java]
            val isDarkTheme by themeViewModel.isDarkMode.collectAsState()

            UniversityBudgetAppTheme(darkTheme = isDarkTheme) {
                BudgetApp(
                    isDarkTheme = isDarkTheme,
                    onThemeChange = { themeViewModel.setDarkMode(it) },
                    notificationViewModel = notificationViewModel
                )
            }
        }
    }
}
