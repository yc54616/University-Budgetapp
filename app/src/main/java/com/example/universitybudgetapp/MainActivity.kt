package com.example.universitybudgetapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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

class MainActivity : ComponentActivity() {

    private lateinit var notificationViewModel: NotificationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1️⃣ DB와 Repository 생성
        val database = AppDatabase.getInstance(applicationContext)
        val repository = NotificationRepository(database.notificationDao())

        // 2️⃣ ViewModelFactory로 ViewModel 생성
        val factory = NotificationViewModelFactory(repository)
        notificationViewModel = ViewModelProvider(this, factory)[NotificationViewModel::class.java]

        // 3️⃣ LocalBroadcastManager 등록
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                intent?.let {
                    val title = it.getStringExtra("title") ?: ""
                    val description = it.getStringExtra("description") ?: ""
                    val amount = it.getLongExtra("amount", 0L)  // 수정됨!
                    val type = it.getStringExtra("type") ?: "기타"  // 추가됨!
                    val item = NotificationItem(
                        title = title,
                        description = description,
                        amount = amount,
                        type = type
                    )
                    notificationViewModel.addNotification(item)
                }
            }
        }
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(receiver, IntentFilter("BANK_NOTIFICATION_EVENT"))

        // 4️⃣ Compose Content
        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }
            UniversityBudgetAppTheme(darkTheme = isDarkTheme) {
                BudgetApp(
                    isDarkTheme = isDarkTheme,
                    onThemeChange = { isDarkTheme = it },
                    notificationViewModel = notificationViewModel
                )
            }
        }
    }
}
