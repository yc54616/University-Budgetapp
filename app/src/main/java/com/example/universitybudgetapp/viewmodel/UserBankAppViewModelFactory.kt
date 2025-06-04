// com/example/universitybudgetapp/viewmodel/UserBankAppViewModelFactory.kt
package com.example.universitybudgetapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.universitybudgetapp.data.db.AppDatabase
import com.example.universitybudgetapp.data.db.DatabaseProvider
import com.example.universitybudgetapp.data.repository.UserBankAppRepository

class UserBankAppViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = AppDatabase.getInstance(application).userBankAppDao()
        val repo = UserBankAppRepository(db)
        if (modelClass.isAssignableFrom(UserBankAppViewModel::class.java)) {
            return UserBankAppViewModel(application, repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
