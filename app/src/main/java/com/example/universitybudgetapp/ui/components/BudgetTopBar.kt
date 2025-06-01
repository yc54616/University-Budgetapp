package com.example.universitybudgetapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetTopBar(
    title: String,
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit,
    onTitleClick: () -> Unit,
    onEmailClick: () -> Unit,
    showBadge: Boolean = false,
    showEmailIcon: Boolean = true
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onTitleClick() },
                textAlign = TextAlign.Center
            )
        },
        navigationIcon = {
            IconButton(onClick = onPrevClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "이전 달")
            }
        },
        actions = {
            IconButton(onClick = onNextClick) {
                Icon(Icons.Default.ArrowForward, contentDescription = "다음 달")
            }
            if (showEmailIcon) {
                Box(
                    contentAlignment = Alignment.TopEnd,
                    modifier = Modifier
                        .wrapContentSize()
                ) {
                    IconButton(onClick = onEmailClick) {
                        Icon(Icons.Default.Email, contentDescription = "알림")
                    }
                    if (showBadge) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)  // 🔥 살짝 크게 조절 가능
                                .offset(x = (-10).dp, y = 12.dp)  // 🔥 아이콘 위로 살짝 이동
                                .background(
                                    color = MaterialTheme.colorScheme.error,
                                    shape = CircleShape
                                )
                        )
                    }
                }
            }

        }
    )
}
