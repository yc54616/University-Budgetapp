package com.example.universitybudgetapp.ui.screens

import android.app.DatePickerDialog
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.universitybudgetapp.data.db.AppDatabase
import com.example.universitybudgetapp.data.model.Category
import com.example.universitybudgetapp.data.model.Entry
import com.example.universitybudgetapp.ui.components.CategorySelector
import com.example.universitybudgetapp.viewmodel.EntryViewModel
import com.example.universitybudgetapp.viewmodel.UserCategoryViewModel
import com.example.universitybudgetapp.viewmodel.UserCategoryViewModelFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun AddEntryScreen(
    navController: NavController,
    viewModel: EntryViewModel = viewModel()
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val dao = remember { AppDatabase.getInstance(context).userCategoryDao() }
    val userCategoryViewModel: UserCategoryViewModel =
        viewModel(factory = UserCategoryViewModelFactory(dao))

    val dateFormatter = remember { DateTimeFormatter.ofPattern("yyyy-MM-dd") }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val datePicker = remember {
        DatePickerDialog(
            context,
            { _, year, month, day ->
                selectedDate = LocalDate.of(year, month + 1, day)
            },
            selectedDate.year,
            selectedDate.monthValue - 1,
            selectedDate.dayOfMonth
        )
    }

    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Category>(Category.기타지출) }
    var selectedType by remember { mutableStateOf(Category.Type.EXPENSE) }

    var showKeypad by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }
    val amountFocusState = remember { mutableStateOf(false) }

    BackHandler { navController.popBackStack() }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "뒤로")
        }

        Spacer(Modifier.height(8.dp))
        Text("수입/지출 입력", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = amount.toLongOrNull()?.let { "%,d".format(it) } ?: "",
            onValueChange = {}, // 입력 막기, 대신 CustomKeypad로만
            readOnly = true,
            label = { Text("금액") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    amountFocusState.value = focusState.isFocused
                    if (focusState.isFocused) {
                        showKeypad = true
                        keyboardController?.hide()
                    }
                }
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("설명") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = selectedType == Category.Type.INCOME,
                onClick = {
                    selectedType = Category.Type.INCOME
                    selectedCategory = Category.부수입
                }
            )
            Text("수입", Modifier.padding(start = 4.dp))
            Spacer(Modifier.width(8.dp))
            RadioButton(
                selected = selectedType == Category.Type.EXPENSE,
                onClick = {
                    selectedType = Category.Type.EXPENSE
                    selectedCategory = Category.식비
                }
            )
            Text("지출", Modifier.padding(start = 4.dp))
        }
        Spacer(Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("날짜: ${selectedDate.format(dateFormatter)}")
            Spacer(Modifier.width(8.dp))
            Button(onClick = { datePicker.show() }) {
                Text("날짜 선택")
            }
        }
        Spacer(Modifier.height(16.dp))

        CategorySelector(
            selected = selectedCategory,
            selectedType = selectedType,
            onSelected = { selectedCategory = it },
            userCategoryViewModel = userCategoryViewModel
        )
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                val isIncome = selectedType == Category.Type.INCOME
                viewModel.insertEntry(
                    Entry(
                        amount = amount.toLongOrNull() ?: 0L,
                        description = description,
                        isIncome = isIncome,
                        date = selectedDate,
                        category = selectedCategory
                    )
                )
                navController.popBackStack()
            },
            modifier = Modifier.align(Alignment.Start)
        ) {
            Icon(Icons.Default.Add, contentDescription = "저장")
            Spacer(Modifier.width(4.dp))
            Text("저장")
        }

        Spacer(Modifier.height(16.dp))

        if (showKeypad && amountFocusState.value) {
            CustomKeypad(
                onNumberClick = { digit -> amount += digit },
                onDeleteClick = { amount = amount.dropLast(1) },
                onCloseClick = { showKeypad = false }
            )
        }
    }
}


@Composable
fun CustomKeypad(
    onNumberClick: (String) -> Unit,
    onDeleteClick: () -> Unit,
    onCloseClick: () -> Unit
) {
    val keys = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf("0", "삭제", "닫기")
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        keys.forEach { row ->
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                row.forEach { key ->
                    Button(
                        onClick = {
                            when (key) {
                                "삭제" -> onDeleteClick()
                                "닫기" -> onCloseClick()
                                else -> onNumberClick(key)
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(
                            text = key,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}

