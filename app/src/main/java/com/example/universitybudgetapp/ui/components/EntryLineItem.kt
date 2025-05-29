package com.example.universitybudgetapp.ui.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.universitybudgetapp.data.model.Entry
import com.example.universitybudgetapp.data.model.Category

// 🔧 색상 복원 유틸
@Composable
fun EntryLineItem(entry: Entry) {
    val amountColor = if (entry.isIncome)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.error

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            entry.category?.let {
                Log.d("EntryCheck", "category=$it")

                Icon(
                    imageVector = it.icon,
                    contentDescription = it.name,
                    tint = MaterialTheme.colorScheme.onSurface, // ✅ 항상 동일한 색상으로 고정
                    modifier = Modifier
                        .size(20.dp)
                        .padding(end = 6.dp)
                )

                Text(
                    text = it.name,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            if (entry.description.isNotBlank()) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = entry.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Text(
            text = "%,d원".format(entry.amount),
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = amountColor
        )
    }

    Divider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
}
