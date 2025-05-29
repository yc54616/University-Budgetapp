package com.example.universitybudgetapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.universitybudgetapp.data.db.UserCategoryDao

class UserCategoryViewModelFactory(
    private val dao: UserCategoryDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserCategoryViewModel::class.java)) {
            return UserCategoryViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
