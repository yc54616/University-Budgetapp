package com.example.universitybudgetapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.universitybudgetapp.ui.BudgetApp
import com.example.universitybudgetapp.ui.theme.UniversityBudgetAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UniversityBudgetAppTheme {
                BudgetApp()
            }
        }
    }
}
