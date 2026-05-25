package com.lifeos.presentation.todo

import com.lifeos.domain.model.Priority
import com.lifeos.domain.model.Todo

data class TodoListUiState(
    val isLoading: Boolean = true,
    val todos: List<Todo> = emptyList(),
    val errorMessage: String? = null,
    val sortKeys: List<TodoSortKey> = listOf(TodoSortKey.Updated)
) {
    val isEmpty: Boolean get() = !isLoading && todos.isEmpty() && errorMessage == null
}

/** Sort direction for a key. */
enum class SortDirection { ASC, DESC }

/** Sortable fields for Todos (direction stored separately in [TodoSortKey]). */
enum class TodoSortField { Updated, DueDate, Priority }

/** A single sort key in an ORDER BY chain. */
data class TodoSortKey(
    val field: TodoSortField,
    val direction: SortDirection
) {
    companion object {
        val Updated = TodoSortKey(TodoSortField.Updated, SortDirection.DESC)
        val DueDate = TodoSortKey(TodoSortField.DueDate, SortDirection.ASC)
        val Priority = TodoSortKey(TodoSortField.Priority, SortDirection.DESC)
    }
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
