package com.example.universitybudgetapp.data.db

import androidx.room.*
import com.example.universitybudgetapp.data.model.NotificationItem
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY id DESC")
    fun getAllNotifications(): Flow<List<NotificationItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationItem)

    @Delete
    suspend fun deleteNotification(notification: NotificationItem)
}
