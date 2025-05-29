package com.example.universitybudgetapp.ui.screens

import android.app.DatePickerDialog
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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

    // UserCategoryViewModel 생성
    val dao = remember { AppDatabase.getInstance(context).userCategoryDao() }
    val userCategoryViewModel: UserCategoryViewModel =
        viewModel(factory = UserCategoryViewModelFactory(dao))

    // 날짜 포맷터 & LocalDate 상태
    val dateFormatter = remember { DateTimeFormatter.ofPattern("yyyy-MM-dd") }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val datePicker = remember {
        DatePickerDialog(
            context,
            { _, year, month, day ->
                // month는 0-based 이므로 +1
                selectedDate = LocalDate.of(year, month + 1, day)
            },
            selectedDate.year,
            selectedDate.monthValue - 1,
            selectedDate.dayOfMonth
        )
    }

    // 입력 필드 상태
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isIncome by remember { mutableStateOf(true) }
    var selectedCategory by remember { mutableStateOf<Category>(Category.기타) }

    // 뒤로가기
    BackHandler { navController.popBackStack() }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 상단 뒤로 버튼
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "뒤로")
        }

        Spacer(Modifier.height(8.dp))
        Text("수입/지출 입력", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        // 금액
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("금액") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        // 설명
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("설명") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        // 수입/지출 라디오
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("구분:")
            Spacer(Modifier.width(8.dp))
            RadioButton(selected = isIncome, onClick = { isIncome = true })
            Text("수입")
            Spacer(Modifier.width(8.dp))
            RadioButton(selected = !isIncome, onClick = { isIncome = false })
            Text("지출")
        }
        Spacer(Modifier.height(8.dp))

        // 날짜 선택
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("날짜: ${selectedDate.format(dateFormatter)}")
            Spacer(Modifier.width(8.dp))
            Button(onClick = { datePicker.show() }) {
                Text("날짜 선택")
            }
        }
        Spacer(Modifier.height(16.dp))

        // 카테고리 선택
        CategorySelector(
            selected = selectedCategory,
            onSelected = { selectedCategory = it },
            userCategoryViewModel = userCategoryViewModel
        )
        Spacer(Modifier.height(16.dp))

        // 저장 버튼
        Button(
            onClick = {
                viewModel.insertEntry(
                    Entry(
                        amount      = amount.toIntOrNull() ?: 0,
                        description = description,
                        isIncome    = isIncome,
                        date        = selectedDate,
                        category    = selectedCategory
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
    }
}
