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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lifeos.R
import com.lifeos.core.util.DateTimeUtils
import com.lifeos.domain.model.Priority
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTodoScreen(
    state: AddEditTodoUiState,
    onBack: () -> Unit,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onPriorityChange: (Priority) -> Unit,
    onDueDateChange: (Long?) -> Unit,
    onCompletedChange: (Boolean) -> Unit,
    onSave: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(
                            if (state.isEdit) R.string.title_edit_todo else R.string.title_add_todo
                        )
                    )
                },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text(stringResource(R.string.action_back)) }
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
                onValueChange = onTitleChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.todo_title_label)) },
                singleLine = true,
                enabled = !state.isSaving
            )

            Spacer(Modifier.padding(8.dp))

            OutlinedTextField(
                value = state.description,
                onValueChange = onDescriptionChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.todo_description_label)) },
                enabled = !state.isSaving
            )

            Spacer(Modifier.padding(8.dp))

            PriorityPicker(priority = state.priority, onPriority = onPriorityChange)

            Spacer(Modifier.padding(8.dp))

            DueDatePicker(
                dueDateMillis = state.dueDateMillis,
                enabled = !state.isSaving,
                onPick = { onDueDateChange(it) },
                onClear = { onDueDateChange(null) }
            )

            Spacer(Modifier.padding(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = state.isCompleted,
                    onCheckedChange = onCompletedChange,
                    enabled = !state.isSaving
                )
                Text(stringResource(R.string.todo_completed_label))
            }

            if (state.errorMessage != null) {
                Spacer(Modifier.padding(8.dp))
                Text(state.errorMessage, color = MaterialTheme.colorScheme.error)
            }

            Spacer(Modifier.padding(12.dp))

            Button(
                onClick = onSave,
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isSaving
            ) {
                Text(
                    stringResource(
                        if (state.isSaving) R.string.action_saving else R.string.action_save
                    )
                )
            }
        }
    }
}

@Composable
private fun PriorityPicker(priority: Priority, onPriority: (Priority) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(stringResource(R.string.todo_priority_label))
        Spacer(Modifier.padding(6.dp))
        Priority.values().forEach { p ->
            TextButton(onClick = { onPriority(p) }) {
                // Brackets indicate the selected item (MEDIUM is selected by default)
                Text(if (p == priority) "[${p.name}]" else p.name)
            }
        }
    }
}

@Composable
private fun DueDatePicker(
    dueDateMillis: Long?,
    enabled: Boolean,
    onPick: (Long) -> Unit,
    onClear: () -> Unit
) {
    val context = LocalContext.current
    val label = dueDateMillis?.let {
        stringResource(R.string.todo_due_prefix, DateTimeUtils.formatDate(it))
    } ?: stringResource(R.string.todo_due_none)

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(label)
        Spacer(Modifier.padding(6.dp))
        TextButton(
            enabled = enabled,
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
        ) { Text(stringResource(R.string.action_pick)) }
        if (dueDateMillis != null) {
            TextButton(enabled = enabled, onClick = onClear) { Text(stringResource(R.string.action_clear)) }
        }
    }
}
