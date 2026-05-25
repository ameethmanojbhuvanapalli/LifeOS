package com.lifeos.presentation.todo

import com.lifeos.domain.model.Priority
import com.lifeos.domain.model.Todo

data class TodoListUiState(
    val isLoading: Boolean = true,
    val todos: List<Todo> = emptyList(),
    val errorMessage: String? = null,
    val sort: TodoSort = TodoSort.UPDATED_DESC
) {
    val isEmpty: Boolean get() = !isLoading && todos.isEmpty() && errorMessage == null
}

enum class TodoSort {
    UPDATED_DESC,
    DUE_ASC,
    PRIORITY_DESC
}

data class AddEditTodoUiState(
    val isEdit: Boolean = false,
    val id: String? = null,
    val title: String = "",
    val description: String = "",
    val priority: Priority = Priority.MEDIUM,
    val dueDateMillis: Long? = null,
    val isCompleted: Boolean = false,
    val isSaving: Boolean = false,
    val errorMessage: String? = null
)
