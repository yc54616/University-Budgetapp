package com.example.universitybudgetapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetTopBar(
    title: String,
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit,
    onTitleClick: () -> Unit,    // ← 추가
    onEmailClick: () -> Unit,
    showBadge: Boolean,
    showEmailIcon: Boolean  // ← 새 플래그
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onTitleClick() }, // ← 클릭 가능하게
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
            // 🔥 showEmailIcon 이 true 일 때만 이메일 버튼 렌더링
            if (showEmailIcon) {
                IconButton(onClick = onEmailClick) {
                    Box {
                        Icon(Icons.Default.Email, contentDescription = "메일")
                        if (showBadge) {
                            Box(
                                Modifier
                                    .size(6.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.error,
                                        shape = CircleShape
                                    )
                                    .align(Alignment.TopEnd)
                                    .offset(x = 1.dp, y = (-1).dp)
                            )
                        }
                    }
                }
            }
        }
    )
}


