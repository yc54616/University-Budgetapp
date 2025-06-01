package com.example.universitybudgetapp.data.repository

import com.example.universitybudgetapp.data.db.NotificationDao
import com.example.universitybudgetapp.data.model.NotificationItem
import kotlinx.coroutines.flow.Flow

class NotificationRepository(private val dao: NotificationDao) {
    fun getAllNotifications(): Flow<List<NotificationItem>> = dao.getAllNotifications()

    suspend fun insertNotification(item: NotificationItem) = dao.insertNotification(item)

    suspend fun deleteNotification(item: NotificationItem) = dao.deleteNotification(item)
}
