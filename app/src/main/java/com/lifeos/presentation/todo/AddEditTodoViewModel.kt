package com.lifeos.presentation.todo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifeos.domain.model.Priority
import com.lifeos.domain.model.SyncStatus
import com.lifeos.domain.model.Todo
import com.lifeos.domain.usecase.todo.AddOrUpdateTodoUseCase
import com.lifeos.domain.usecase.todo.GetTodosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AddEditTodoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val addOrUpdateTodoUseCase: AddOrUpdateTodoUseCase,
    private val getTodosUseCase: GetTodosUseCase
) : ViewModel() {

    private val todoId: String? = savedStateHandle["todoId"]

    private val _uiState = MutableStateFlow(AddEditTodoUiState(isEdit = todoId != null, id = todoId))
    val uiState: StateFlow<AddEditTodoUiState> = _uiState.asStateFlow()

    init {
        if (todoId != null) {
            load(todoId)
        }
    }

    fun setTitle(value: String) = _uiState.update { it.copy(title = value, errorMessage = null) }
    fun setDescription(value: String) = _uiState.update { it.copy(description = value, errorMessage = null) }
    fun setPriority(value: Priority) = _uiState.update { it.copy(priority = value, errorMessage = null) }
    fun setDueDate(millis: Long?) = _uiState.update { it.copy(dueDateMillis = millis, errorMessage = null) }
    fun setCompleted(value: Boolean) = _uiState.update { it.copy(isCompleted = value, errorMessage = null) }

    fun save(onSaved: () -> Unit) {
        viewModelScope.launch {
            val state = _uiState.value
            val title = state.title.trim()
            if (title.isBlank()) {
                _uiState.update { it.copy(errorMessage = "Title is required") }
                return@launch
            }

            _uiState.update { it.copy(isSaving = true) }

            val now = System.currentTimeMillis()
            val id = state.id ?: UUID.randomUUID().toString()

            val todo = Todo(
                id = id,
                title = title,
                description = state.description.trim().ifBlank { null },
                isCompleted = state.isCompleted,
                priority = state.priority,
                dueDate = state.dueDateMillis,
                createdAt = now,
                updatedAt = now,
                syncStatus = SyncStatus.LOCAL_ONLY
            )

            runCatching { addOrUpdateTodoUseCase(todo) }
                .onSuccess {
                    _uiState.update { it.copy(isSaving = false) }
                    onSaved()
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isSaving = false, errorMessage = e.message ?: "Failed to save") }
                }
        }
    }

    private fun load(id: String) {
        viewModelScope.launch {
            val existing = getTodosUseCase().first().firstOrNull { it.id == id } ?: return@launch
            _uiState.update {
                it.copy(
                    title = existing.title,
                    description = existing.description.orEmpty(),
                    priority = existing.priority,
                    dueDateMillis = existing.dueDate,
                    isCompleted = existing.isCompleted
                )
            }
        }
    }
}
