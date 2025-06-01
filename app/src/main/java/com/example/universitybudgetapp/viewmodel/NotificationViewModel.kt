package com.example.universitybudgetapp.viewmodel

import androidx.compose.runtime.mutableStateOf
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

    // ✅ dismissed 상태 추가
    private val _alreadyDismissed = mutableStateOf(false)
    val alreadyDismissed: Boolean get() = _alreadyDismissed.value

    fun markDialogDismissed() {
        _alreadyDismissed.value = true
    }

    fun resetDialogDismissed() {
        _alreadyDismissed.value = false
    }

    // ✅ showDialog 상태 추가
    private val _showDialog = mutableStateOf(false)
    val showDialog: Boolean get() = _showDialog.value

    fun showDialog() {
        _showDialog.value = true
    }

    fun hideDialog() {
        _showDialog.value = false
    }

    fun addNotification(item: NotificationItem) {
        viewModelScope.launch {
            repository.insertNotification(item)
            resetDialogDismissed()   // ✅ 새로운 알림 감지 시 dismissed 초기화
        }
    }

    fun removeNotification(item: NotificationItem) {
        viewModelScope.launch {
            repository.deleteNotification(item)
        }
    }
}
