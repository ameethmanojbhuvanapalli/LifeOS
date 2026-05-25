package com.lifeos.presentation.todo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AddEditTodoRoute(
    onBack: () -> Unit,
    viewModel: AddEditTodoViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    AddEditTodoScreen(
        state = state,
        onBack = onBack,
        onTitleChange = viewModel::setTitle,
        onDescriptionChange = viewModel::setDescription,
        onPriorityChange = viewModel::setPriority,
        onDueDateChange = viewModel::setDueDate,
        onCompletedChange = viewModel::setCompleted,
        onSave = { viewModel.save(onSaved = onBack) }
    )
}
