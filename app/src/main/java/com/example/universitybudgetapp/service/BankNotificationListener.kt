package com.example.universitybudgetapp.service

import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.example.universitybudgetapp.MainActivity

class BankNotificationListener : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val packageName = sbn.packageName
        val extras = sbn.notification.extras
        val title = extras.getCharSequence("android.title")?.toString() ?: ""
        val text = extras.getCharSequence("android.text")?.toString() ?: ""

        Log.d("BankNotificationListener", "패키지: $packageName, 제목: $title, 내용: $text")

        if (text.contains("민감한 알림 콘텐츠 숨김")) {
            Log.w("BankNotificationListener", "민감한 알림 — 사용자 설정 안내 필요")
            // 앱에서 안내 다이얼로그로 처리
        } else if (packageName.contains("notitest") || title.contains("입금") || text.contains("출금")) {
            val amountRegex = Regex("""(\d{1,3}(,\d{3})*)(원)""")
            val match = amountRegex.find(text)
            val amount = match?.groups?.get(1)?.value?.replace(",", "")?.toIntOrNull() ?: 0

            val intent = Intent(applicationContext, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                putExtra("BANK_NOTIFICATION", true)
                putExtra("amount", amount)
                putExtra("description", text)
            }
            startActivity(intent)
        }
    }
}
