package com.lifeos.presentation.todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun setSort(sort: TodoSort) {
        _uiState.update { it.copy(sort = sort) }
    }

    fun toggle(todo: Todo) {
        viewModelScope.launch {
            toggleTodoUseCase(todo)
        }
    }

    fun delete(todo: Todo) {
        viewModelScope.launch {
            deleteTodoUseCase(todo.id)
        }
    }

    fun sync() {
        viewModelScope.launch {
            runCatching { syncTodosUseCase() }
        }
    }

    private fun observeTodos() {
        viewModelScope.launch {
            getTodosUseCase()
                .catch { e ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = e.message ?: "Unknown error") }
                }
                .collectLatest { list ->
                    _uiState.update { state ->
                        val sorted = sortTodos(list, state.sort)
                        state.copy(isLoading = false, todos = sorted, errorMessage = null)
                    }
                }
        }
    }

    private fun sortTodos(list: List<Todo>, sort: TodoSort): List<Todo> {
        return when (sort) {
            TodoSort.UPDATED_DESC -> list.sortedByDescending { it.updatedAt }
            TodoSort.DUE_ASC -> list.sortedWith(compareBy(nullsLast()) { it.dueDate })
            TodoSort.PRIORITY_DESC -> list.sortedByDescending { it.priority.ordinal }
        }
    }
}
