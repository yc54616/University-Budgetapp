package com.example.universitybudgetapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import com.example.universitybudgetapp.data.model.Entry

@Composable
fun EntryRow(entry: Entry) {
    val amountColor = if (entry.isIncome)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.error

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = if (entry.isIncome) "수입" else "지출",
                    style = MaterialTheme.typography.bodyMedium
                )
                if (entry.description.isNotBlank()) {
                    Text(
                        text = entry.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Text(
                text = "${entry.amount}원",
                style = MaterialTheme.typography.bodyMedium,
                color = amountColor
            )
        }

        Divider(modifier = Modifier.padding(top = 8.dp))
    }
}
