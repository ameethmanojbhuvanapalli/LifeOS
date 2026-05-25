package com.lifeos.presentation.todo

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lifeos.R
import com.lifeos.core.util.DateTimeUtils
import com.lifeos.domain.model.Todo

private enum class TodoFilter { ALL, ACTIVE, COMPLETED }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListRoute(
    onAdd: () -> Unit,
    onEdit: (String) -> Unit,
    viewModel: TodoListViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { snackbarHostState.showSnackbar(it) }
    }

    var sortMenuExpanded by remember { mutableStateOf(false) }
    var deleteTarget by remember { mutableStateOf<Todo?>(null) }

    var filter by remember { mutableStateOf(TodoFilter.ALL) }
    var completedExpanded by remember { mutableStateOf(false) }

    if (deleteTarget != null) {
        AlertDialog(
            onDismissRequest = { deleteTarget = null },
            title = { Text(stringResource(R.string.todo_delete_dialog_title)) },
            text = { Text(stringResource(R.string.todo_delete_dialog_message)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        val t = deleteTarget
                        deleteTarget = null
                        if (t != null) viewModel.delete(t)
                    }
                ) { Text(stringResource(R.string.action_delete)) }
            },
            dismissButton = {
                TextButton(onClick = { deleteTarget = null }) { Text(stringResource(R.string.action_cancel)) }
            }
        )
    }

    val activeTodos = state.todos.filter { !it.isCompleted }
    val completedTodos = state.todos.filter { it.isCompleted }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.title_todos)) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface),
                actions = {
                    TextButton(onClick = { sortMenuExpanded = true }) { Text(stringResource(R.string.action_sort)) }
                    SortMenu(
                        expanded = sortMenuExpanded,
                        onDismiss = { sortMenuExpanded = false },
                        sortKeys = state.sortKeys,
                        onToggleField = viewModel::toggleSortField,
                        onRemoveField = viewModel::removeSortField,
                        onClear = viewModel::clearSort
                    )
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAdd) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.action_add))
            }
        }
    ) { padding ->
        if (state.isEmpty) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(stringResource(R.string.todo_empty_title))
                Text(stringResource(R.string.todo_empty_subtitle), style = MaterialTheme.typography.bodyMedium)
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(label = stringResource(R.string.todo_filter_all), selected = filter == TodoFilter.ALL) { filter = TodoFilter.ALL }
                FilterChip(label = stringResource(R.string.todo_filter_active), selected = filter == TodoFilter.ACTIVE) { filter = TodoFilter.ACTIVE }
                FilterChip(label = stringResource(R.string.todo_filter_completed), selected = filter == TodoFilter.COMPLETED) { filter = TodoFilter.COMPLETED }
            }

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                when (filter) {
                    TodoFilter.ALL -> {
                        if (activeTodos.isNotEmpty()) {
                            item { SectionHeader(stringResource(R.string.todo_section_todo), count = activeTodos.size) }
                            items(activeTodos, key = { it.id }) { todo ->
                                TodoRow(
                                    todo = todo,
                                    onToggle = { viewModel.toggle(todo) },
                                    onClick = { onEdit(todo.id) },
                                    onDelete = { deleteTarget = todo }
                                )
                            }
                        }

                        if (completedTodos.isNotEmpty()) {
                            item {
                                CompletedHeader(
                                    count = completedTodos.size,
                                    expanded = completedExpanded,
                                    onToggle = { completedExpanded = !completedExpanded }
                                )
                            }
                            if (completedExpanded) {
                                items(completedTodos, key = { it.id }) { todo ->
                                    TodoRow(
                                        todo = todo,
                                        onToggle = { viewModel.toggle(todo) },
                                        onClick = { onEdit(todo.id) },
                                        onDelete = { deleteTarget = todo }
                                    )
                                }
                            }
                        }
                    }

                    TodoFilter.ACTIVE -> {
                        items(activeTodos, key = { it.id }) { todo ->
                            TodoRow(
                                todo = todo,
                                onToggle = { viewModel.toggle(todo) },
                                onClick = { onEdit(todo.id) },
                                onDelete = { deleteTarget = todo }
                            )
                        }
                    }

                    TodoFilter.COMPLETED -> {
                        items(completedTodos, key = { it.id }) { todo ->
                            TodoRow(
                                todo = todo,
                                onToggle = { viewModel.toggle(todo) },
                                onClick = { onEdit(todo.id) },
                                onDelete = { deleteTarget = todo }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SortMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    sortKeys: List<TodoSortKey>,
    onToggleField: (TodoSortField) -> Unit,
    onRemoveField: (TodoSortField) -> Unit,
    onClear: () -> Unit
) {
    DropdownMenu(expanded = expanded, onDismissRequest = onDismiss) {
        SortMenuItem(
            title = stringResource(R.string.todo_sort_due_date),
            field = TodoSortField.DueDate,
            sortKeys = sortKeys,
            onToggleField = onToggleField,
            onRemoveField = onRemoveField
        )
        SortMenuItem(
            title = stringResource(R.string.todo_sort_priority),
            field = TodoSortField.Priority,
            sortKeys = sortKeys,
            onToggleField = onToggleField,
            onRemoveField = onRemoveField
        )
        SortMenuItem(
            title = stringResource(R.string.todo_sort_updated),
            field = TodoSortField.Updated,
            sortKeys = sortKeys,
            onToggleField = onToggleField,
            onRemoveField = onRemoveField
        )

        DropdownMenuItem(
            text = { Text(stringResource(R.string.action_clear)) },
            onClick = {
                onClear()
                onDismiss()
            }
        )
    }
}

@Composable
private fun SortMenuItem(
    title: String,
    field: TodoSortField,
    sortKeys: List<TodoSortKey>,
    onToggleField: (TodoSortField) -> Unit,
    onRemoveField: (TodoSortField) -> Unit
) {
    val index = sortKeys.indexOfFirst { it.field == field }
    val order = if (index >= 0) "${index + 1}" else null
    val dir = sortKeys.getOrNull(index)?.direction

    val suffix = when {
        order == null -> ""
        dir == SortDirection.ASC -> "  $order ↑"
        else -> "  $order ↓"
    }

    DropdownMenuItem(
        text = { Text(title + suffix) },
        onClick = { onToggleField(field) }
    )

    // Note: removal UI can be added later (e.g., trailing icon). For now: Clear resets everything.
}

@Composable
private fun FilterChip(label: String, selected: Boolean, onClick: () -> Unit) {
    AssistChip(
        onClick = onClick,
        label = { Text(label) },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
            labelColor = if (selected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}

@Composable
private fun SectionHeader(title: String, count: Int) {
    Text(
        text = "$title ($count)",
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
        style = MaterialTheme.typography.titleSmall
    )
}

@Composable
private fun CompletedHeader(count: Int, expanded: Boolean, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.todo_section_completed) + " ($count)",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = onToggle) {
            Icon(
                imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = stringResource(if (expanded) R.string.action_collapse else R.string.action_expand)
            )
        }
    }
}

@Composable
private fun TodoRow(
    todo: Todo,
    onToggle: () -> Unit,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val completedStyle = if (todo.isCompleted) TextDecoration.LineThrough else null
    val completedAlpha = if (todo.isCompleted) 0.6f else 1f

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = todo.isCompleted, onCheckedChange = { onToggle() })
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
                .alpha(completedAlpha)
                .combinedClickable(
                    onClick = onClick,
                    onLongClick = onDelete
                )
        ) {
            Text(
                todo.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textDecoration = completedStyle
            )

            val subtitle = buildString {
                append(todo.priority.name)
                todo.dueDate?.let {
                    append(" • ")
                    append(stringResource(R.string.todo_due_prefix, DateTimeUtils.formatDate(it)))
                }
                if (!todo.description.isNullOrBlank()) {
                    append(" • ")
                    append(todo.description)
                }
            }

            Text(
                subtitle,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.action_delete))
        }
    }
}
