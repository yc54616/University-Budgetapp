package com.example.universitybudgetapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.universitybudgetapp.data.db.DatabaseProvider
import com.example.universitybudgetapp.data.model.Entry
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class EntryViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = DatabaseProvider.getDatabase(application).entryDao()

    val entries: StateFlow<List<Entry>> = dao.getAllEntries()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun insertEntry(entry: Entry) {
        viewModelScope.launch {
            dao.insert(entry)
        }
    }

    fun updateEntry(entry: Entry) {
        viewModelScope.launch {
            dao.update(entry)
        }
    }

    fun deleteEntry(entry: Entry) {
        viewModelScope.launch {
            dao.delete(entry)
        }
    }
    fun undoRefund(entry: Entry, refundAmount: Long) {
        viewModelScope.launch {
            val updatedAmount = if (!entry.isIncome) {
                entry.amount + refundAmount  // 수입 환급 취소: 금액 복원
            } else {
                (entry.amount - refundAmount).coerceAtLeast(0L)  // 지출 환급 취소: 금액 다시 빼기
            }
            val updatedEntry = entry.copy(amount = updatedAmount)
            dao.update(updatedEntry)
        }
    }



    fun deleteAllEntries() {
        viewModelScope.launch {
            dao.deleteAll()
        }
    }

    fun getEntryById(id: Int?): Entry? {
        return entries.value.find { it.id == id }
    }

    // 🔥 DB 직접 조회 함수 추가
    fun getEntryByIdFromDb(id: Int, onResult: (Entry?) -> Unit) {
        viewModelScope.launch {
            val entry = dao.getEntryById(id)
            onResult(entry)
        }
    }


    /**
     * 환급(차감) 기능
     * 선택한 Entry에서 금액(amount)에서 환급금액을 빼줌
     */
    fun processRefund(entry: Entry, refundAmount: Long) {
        viewModelScope.launch {
            val updatedAmount = if (!entry.isIncome) {
                (entry.amount - refundAmount).coerceAtLeast(0L)  // 수입 환급: 금액 감소
            } else {
                entry.amount + refundAmount  // 지출 환급: 금액 증가
            }
            val updatedEntry = entry.copy(amount = updatedAmount)
            dao.update(updatedEntry)
        }
    }

}
