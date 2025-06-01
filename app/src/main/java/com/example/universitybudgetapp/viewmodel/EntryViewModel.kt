package com.example.universitybudgetapp.viewmodel

import android.app.Application
import androidx.lifecycle.*
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

    // ✅ 전체 삭제 로직 추가
    fun deleteAllEntries() {
        viewModelScope.launch {
            dao.deleteAll()
        }
    }

    // ID로 Entry 가져오기
    fun getEntryById(id: Int?): Entry? {
        return entries.value.find { it.id == id }
    }

    fun deleteEntry(entry: Entry) {
        viewModelScope.launch {
            dao.delete(entry)
        }
    }

}
