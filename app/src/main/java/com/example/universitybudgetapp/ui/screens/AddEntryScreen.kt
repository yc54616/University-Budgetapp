package com.example.universitybudgetapp.ui.screens

import android.app.DatePickerDialog
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.universitybudgetapp.data.db.AppDatabase
import com.example.universitybudgetapp.data.model.Entry
import com.example.universitybudgetapp.data.model.Category
import com.example.universitybudgetapp.ui.components.CategorySelector
import com.example.universitybudgetapp.viewmodel.EntryViewModel
import com.example.universitybudgetapp.viewmodel.UserCategoryViewModel
import com.example.universitybudgetapp.viewmodel.UserCategoryViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AddEntryScreen(
    viewModel: EntryViewModel = viewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val dao = remember { AppDatabase.getInstance(context).userCategoryDao() }
    val userCategoryViewModel: UserCategoryViewModel = viewModel(
        factory = UserCategoryViewModelFactory(dao)
    )

    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }
    val calendar = remember { Calendar.getInstance() }

    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isIncome by remember { mutableStateOf(true) }
    var selectedDate by remember { mutableStateOf(dateFormat.format(calendar.time)) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }

    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                selectedDate = dateFormat.format(calendar.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    // 시스템 뒤로가기 핸들러
    BackHandler {
        navController.popBackStack()
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        // 상단 ← 버튼
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("수입/지출 입력", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("금액") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("설명") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("구분:")
            Spacer(modifier = Modifier.width(8.dp))
            RadioButton(selected = isIncome, onClick = { isIncome = true })
            Text("수입")
            Spacer(modifier = Modifier.width(8.dp))
            RadioButton(selected = !isIncome, onClick = { isIncome = false })
            Text("지출")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("날짜: $selectedDate")
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { datePickerDialog.show() }) {
                Text("날짜 선택")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ✅ 카테고리 선택 UI 추가
        CategorySelector(
            selected = selectedCategory ?: Category.기타,
            onSelected = { selectedCategory = it },
            userCategoryViewModel = userCategoryViewModel
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val entry = Entry(
                    amount = amount.toIntOrNull() ?: 0,
                    description = description,
                    isIncome = isIncome,
                    date = selectedDate,
                    category = selectedCategory ?: Category.기타
                )
                viewModel.insertEntry(entry)
                navController.popBackStack()
            },
            modifier = Modifier.align(Alignment.Start)
        ) {
            Text("저장")
        }
    }
}
