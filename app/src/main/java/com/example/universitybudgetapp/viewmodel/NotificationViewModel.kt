package com.example.universitybudgetapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.universitybudgetapp.data.model.Entry
import com.example.universitybudgetapp.data.model.NotificationItem
import com.example.universitybudgetapp.data.repository.NotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val repository: NotificationRepository
) : ViewModel() {

    val notifications = repository.getAllNotifications()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _alreadyDismissed = mutableStateOf(false)
    val alreadyDismissed: Boolean get() = _alreadyDismissed.value

    fun markDialogDismissed() {
        _alreadyDismissed.value = true
    }

    fun resetDialogDismissed() {
        _alreadyDismissed.value = false
    }

    private val _showDialog = mutableStateOf(false)
    val showDialog: Boolean get() = _showDialog.value

    fun showDialog() {
        _showDialog.value = true
    }

    fun hideDialog() {
        _showDialog.value = false
    }

    private val _recentlyRefundedEntry = MutableStateFlow<Entry?>(null)
    val recentlyRefundedEntry: StateFlow<Entry?> = _recentlyRefundedEntry.asStateFlow()

    private val _recentlyRefundedRefundAmount = MutableStateFlow<Long?>(null)
    val recentlyRefundedRefundAmount: StateFlow<Long?> = _recentlyRefundedRefundAmount.asStateFlow()

    private val _recentlyRefundedNotification = MutableStateFlow<NotificationItem?>(null)
    val recentlyRefundedNotification: StateFlow<NotificationItem?> = _recentlyRefundedNotification.asStateFlow()

    fun setRecentlyRefunded(
        entry: Entry?,
        refundAmount: Long?,
        notificationItem: NotificationItem?
    ) {
        _recentlyRefundedEntry.value = entry
        _recentlyRefundedRefundAmount.value = refundAmount
        _recentlyRefundedNotification.value = notificationItem
    }

    fun clearRecentlyRefunded() {
        _recentlyRefundedEntry.value = null
        _recentlyRefundedRefundAmount.value = null
        _recentlyRefundedNotification.value = null
    }

    fun addNotification(item: NotificationItem) {
        viewModelScope.launch {
            repository.insertNotification(item)
            resetDialogDismissed()
        }
    }

    fun removeNotification(item: NotificationItem) {
        viewModelScope.launch {
            repository.deleteNotification(item)
        }
    }

    fun getNotificationById(id: String?): NotificationItem? {
        return notifications.value.find { it.id == id }
    }
}