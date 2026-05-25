package com.lifeos.presentation.todo

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lifeos.core.util.DateTimeUtils
import com.lifeos.domain.model.Priority
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTodoRoute(
    onBack: () -> Unit,
    viewModel: AddEditTodoViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.value
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (state.isEdit) "Edit Todo" else "Add Todo") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Back") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = state.title,
                onValueChange = viewModel::setTitle,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Title*") },
                singleLine = true
            )

            Spacer(Modifier.padding(8.dp))

            OutlinedTextField(
                value = state.description,
                onValueChange = viewModel::setDescription,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Description") }
            )

            Spacer(Modifier.padding(8.dp))

            PriorityPicker(priority = state.priority, onPriority = viewModel::setPriority)

            Spacer(Modifier.padding(8.dp))

            DueDatePicker(
                dueDateMillis = state.dueDateMillis,
                onPick = viewModel::setDueDate,
                onClear = { viewModel.setDueDate(null) }
            )

            Spacer(Modifier.padding(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = state.isCompleted, onCheckedChange = viewModel::setCompleted)
                Text("Completed")
            }

            if (state.errorMessage != null) {
                Spacer(Modifier.padding(8.dp))
                Text(state.errorMessage, color = MaterialTheme.colorScheme.error)
            }

            Spacer(Modifier.padding(12.dp))

            Button(
                onClick = { viewModel.save(onSaved = onBack) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isSaving
            ) {
                Text(if (state.isSaving) "Saving..." else "Save")
            }
        }
    }
}

@Composable
private fun PriorityPicker(priority: Priority, onPriority: (Priority) -> Unit) {
    // Simple row buttons (can be upgraded to segmented buttons later)
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("Priority:")
        Spacer(Modifier.padding(6.dp))
        Priority.values().forEach { p ->
            TextButton(onClick = { onPriority(p) }) {
                Text(if (p == priority) "[${p.name}]" else p.name)
            }
        }
    }
}

@Composable
private fun DueDatePicker(
    dueDateMillis: Long?,
    onPick: (Long) -> Unit,
    onClear: () -> Unit
) {
    val context = LocalContext.current
    val label = dueDateMillis?.let { "Due: ${DateTimeUtils.formatDate(it)}" } ?: "No due date"

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(label)
        Spacer(Modifier.padding(6.dp))
        TextButton(
            onClick = {
                val cal = Calendar.getInstance()
                val dialog = DatePickerDialog(
                    context,
                    { _, y, m, d ->
                        val localDate = LocalDate.of(y, m + 1, d)
                        val millis = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                        onPick(millis)
                    },
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                )
                dialog.show()
            }
        ) { Text("Pick") }
        if (dueDateMillis != null) {
            TextButton(onClick = onClear) { Text("Clear") }
        }
    }
}
