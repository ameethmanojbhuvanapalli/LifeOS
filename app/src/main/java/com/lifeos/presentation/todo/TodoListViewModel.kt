package com.lifeos.presentation.todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifeos.domain.model.Priority
import com.lifeos.domain.model.Todo
import com.lifeos.domain.usecase.todo.DeleteTodoUseCase
import com.lifeos.domain.usecase.todo.GetTodosUseCase
import com.lifeos.domain.usecase.todo.SyncTodosUseCase
import com.lifeos.domain.usecase.todo.ToggleTodoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val getTodosUseCase: GetTodosUseCase,
    private val toggleTodoUseCase: ToggleTodoUseCase,
    private val deleteTodoUseCase: DeleteTodoUseCase,
    private val syncTodosUseCase: SyncTodosUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TodoListUiState())
    val uiState: StateFlow<TodoListUiState> = _uiState.asStateFlow()

    init {
        observeTodos()
        sync()
    }

    fun toggleSortField(field: TodoSortField) {
        _uiState.update { state ->
            val existingIndex = state.sortKeys.indexOfFirst { it.field == field }

            val newKeys = when {
                // Not in the list: add it (tap 1)
                existingIndex == -1 -> state.sortKeys + defaultKeyFor(field)

                // In the list: check current direction
                state.sortKeys[existingIndex].direction == SortDirection.ASC -> {
                    // Tap 2: toggle to DESC
                    state.sortKeys.mapIndexed { idx, key ->
                        if (idx != existingIndex) key else key.copy(direction = SortDirection.DESC)
                    }
                }

                else -> {
                    // Tap 3: remove it (direction is DESC, so next tap removes)
                    state.sortKeys.filterIndexed { idx, _ -> idx != existingIndex }
                }
            }

            state.copy(sortKeys = newKeys)
        }
    }

    fun toggle(todo: Todo) {
        viewModelScope.launch { toggleTodoUseCase(todo) }
    }

    fun delete(todo: Todo) {
        viewModelScope.launch { deleteTodoUseCase(todo.id) }
    }

    fun sync() {
        viewModelScope.launch { runCatching { syncTodosUseCase() } }
    }

    private fun observeTodos() {
        viewModelScope.launch {
            combine(
                getTodosUseCase(),
                _uiState.map { it.sortKeys }
            ) { list, keys -> sortTodos(list, keys) }
                .catch { e ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = e.message ?: "Unknown error")
                    }
                }
                .collectLatest { sorted ->
                    _uiState.update { state ->
                        state.copy(isLoading = false, todos = sorted, errorMessage = null)
                    }
                }
        }
    }

    private fun sortTodos(list: List<Todo>, keys: List<TodoSortKey>): List<Todo> {
        val effectiveKeys = if (keys.isEmpty()) {
            TodoListUiState.defaultSortKeys
        } else {
            keys
        }

        val stableKeys = (effectiveKeys + TodoSortKey.Updated).distinctBy { it.field }

        val comparator = stableKeys
            .asSequence()
            .map(::comparatorFor)
            .reduce { acc, next -> acc.then(next) }

        return list.sortedWith(comparator)
    }

    private fun comparatorFor(key: TodoSortKey): Comparator<Todo> {
        val base: Comparator<Todo> = when (key.field) {
            TodoSortField.Updated -> Comparator.comparingLong(Todo::updatedAt)
            TodoSortField.DueDate -> Comparator.comparing(
                { t: Todo -> t.dueDate },
                nullsLast(naturalOrder<Long>())
            )
            TodoSortField.Priority -> Comparator.comparingInt { t: Todo -> priorityRank(t.priority) }
        }

        return if (key.direction == SortDirection.ASC) base else base.reversed()
    }

    private fun priorityRank(priority: Priority): Int {
        return when (priority) {
            Priority.HIGH -> 3
            Priority.MEDIUM -> 2
            Priority.LOW -> 1
        }
    }

    private fun defaultKeyFor(field: TodoSortField): TodoSortKey {
        return when (field) {
            TodoSortField.Updated -> TodoSortKey.Updated
            TodoSortField.DueDate -> TodoSortKey.DueDate
            TodoSortField.Priority -> TodoSortKey.Priority
        }
    }
}

private fun SortDirection.flip(): SortDirection = if (this == SortDirection.ASC) SortDirection.DESC else SortDirection.ASC
