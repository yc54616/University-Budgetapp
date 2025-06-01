package com.example.universitybudgetapp.service

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.example.universitybudgetapp.util.NotificationBroadcaster

class BankNotificationListener : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val extras = sbn.notification.extras
        val title = extras.getCharSequence("android.title")?.toString() ?: ""
        val text = extras.getCharSequence("android.text")?.toString() ?: ""

        val amountRegex = Regex("""(\d{1,3}(,\d{3})*(\.\d{1,2})?)\s?(원|₩|KRW)?""")
        val match = amountRegex.find(text)
        val amount = match?.groups?.get(1)?.value
            ?.replace(",", "")
            ?.toDoubleOrNull()
            ?.toLong() ?: 0L

        // 💡 지출 키워드와 수입 키워드 예시
        val expenseKeywords = listOf("출금", "이체", "결제", "송금")
        val incomeKeywords = listOf("입금", "수신", "적립", "급여")

        // 수입/지출 구분 (기본: 수입)
        val isExpense = expenseKeywords.any { text.contains(it) || title.contains(it) }
        val isIncome = incomeKeywords.any { text.contains(it) || title.contains(it) }

        val type = when {
            isExpense -> "지출"
            isIncome -> "수입"
            else -> "기타"  // 혹시 둘 다 없으면 수입으로 처리하거나, 팝업창 띄우기 등
        }

        Log.d("BankNotificationListener", "알림 감지됨: $title - $text ($type)")

        NotificationBroadcaster.broadcastNotification(
            applicationContext,
            title,
            text,
            amount,
            type
        )
    }

}
