import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.universitybudgetapp.data.model.Category.Companion.iconFromName

@Composable
fun AddCategoryDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit // name, iconName
) {
    var name by remember { mutableStateOf("") }
    var selectedIconName by remember { mutableStateOf("Category") }

    val iconOptions = listOf(
        "Restaurant",
        "Movie",
        "ShoppingBag",
        "AttachMoney",
        "Category"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("새 카테고리 추가") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("카테고리 이름") },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text("아이콘 선택")
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    iconOptions.forEach { iconName ->
                        val icon = iconFromName(iconName)
                        IconButton(onClick = { selectedIconName = iconName }) {
                            Icon(
                                icon,
                                contentDescription = iconName,
                                tint = if (selectedIconName == iconName) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (name.isNotBlank()) {
                    onConfirm(name, selectedIconName)
                }
            }) {
                Text("추가")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}
