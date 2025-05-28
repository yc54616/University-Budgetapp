package com.example.universitybudgetapp.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetTopBar() {
    TopAppBar(
        title = {
            Text(
                "2025년 5월",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        navigationIcon = {
            IconButton(onClick = {}) {
                Icon(Icons.Default.ArrowBack, contentDescription = "이전")
            }
        },
        actions = {
            IconButton(onClick = {}) {
                Icon(Icons.Default.ArrowForward, contentDescription = "다음")
            }
            IconButton(onClick = {}) {
                BadgedBox(badge = { Badge { Text("36") } }) {
                    Icon(Icons.Default.Email, contentDescription = "메일")
                }
            }
        }
    )
}
