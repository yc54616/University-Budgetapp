package com.example.universitybudgetapp.util

import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager

object NotificationBroadcaster {
    fun broadcastNotification(
        context: Context,
        title: String,
        description: String,
        amount: Long,
        type: String
    ) {
        val intent = Intent("BANK_NOTIFICATION_EVENT").apply {
            putExtra("title", title)
            putExtra("description", description)
            putExtra("amount", amount)
            putExtra("type", type)  // 추가
        }
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }
}
