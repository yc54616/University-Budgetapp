package com.example.universitybudgetapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.universitybudgetapp.data.model.NotificationItem
import com.example.universitybudgetapp.data.repository.NotificationRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val repository: NotificationRepository
) : ViewModel() {

    val notifications = repository.getAllNotifications()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addNotification(item: NotificationItem) {
        viewModelScope.launch {
            repository.insertNotification(item)
        }
    }

    fun removeNotification(item: NotificationItem) {
        viewModelScope.launch {
            repository.deleteNotification(item)
        }
    }
}
