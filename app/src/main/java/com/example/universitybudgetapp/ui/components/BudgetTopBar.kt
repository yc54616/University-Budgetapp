package com.example.universitybudgetapp.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.universitybudgetapp.ui.helpers.currentBackStackEntry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetTopBar(currentRoute: String?) {
    val navController = rememberNavController()

    Surface(
        shape = RectangleShape,
        shadowElevation = 4.dp
    ) {
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

                if (currentRoute == "home") {
                    IconButton(onClick = { }) {
                        BadgedBox(
                            badge = {
                                Badge(
                                    modifier = Modifier.size(8.dp)
                                ) {}
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "메일"
                            )
                        }
                    }
                }
            }
        )
    }
}

