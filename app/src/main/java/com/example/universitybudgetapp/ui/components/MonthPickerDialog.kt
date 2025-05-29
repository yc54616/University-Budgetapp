package com.example.universitybudgetapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthPickerDialog(
    initialYearMonth: YearMonth,
    yearRange: IntRange = (initialYearMonth.year - 5)..(initialYearMonth.year + 5),
    onDismissRequest: () -> Unit,
    onMonthSelected: (YearMonth) -> Unit
) {
    var expandedYear by remember { mutableStateOf(false) }
    var expandedMonth by remember { mutableStateOf(false) }
    var selectedYear by remember { mutableStateOf(initialYearMonth.year) }
    var selectedMonth by remember { mutableStateOf(initialYearMonth.monthValue) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("월 선택") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                // 연도 드롭다운
                ExposedDropdownMenuBox(
                    expanded = expandedYear,
                    onExpandedChange = { expandedYear = !expandedYear }
                ) {
                    TextField(
                        value = "$selectedYear 년",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("연도") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedYear) },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedYear,
                        onDismissRequest = { expandedYear = false }
                    ) {
                        yearRange.forEach { year ->
                            DropdownMenuItem(
                                text = { Text("$year 년") },
                                onClick = {
                                    selectedYear = year
                                    expandedYear = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 월 드롭다운
                ExposedDropdownMenuBox(
                    expanded = expandedMonth,
                    onExpandedChange = { expandedMonth = !expandedMonth }
                ) {
                    TextField(
                        value = "%02d 월".format(selectedMonth),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("월") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMonth) },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedMonth,
                        onDismissRequest = { expandedMonth = false }
                    ) {
                        (1..12).forEach { month ->
                            DropdownMenuItem(
                                text = { Text("%02d 월".format(month)) },
                                onClick = {
                                    selectedMonth = month
                                    expandedMonth = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onMonthSelected(YearMonth.of(selectedYear, selectedMonth))
            }) {
                Text("확인")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("취소")
            }
        }
    )
}
