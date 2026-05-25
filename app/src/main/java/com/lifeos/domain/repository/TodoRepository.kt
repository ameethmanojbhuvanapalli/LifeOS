package com.lifeos.domain.repository

import com.lifeos.domain.model.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    fun getAllTodos(): Flow<List<Todo>>
    suspend fun upsertTodo(todo: Todo)
    suspend fun deleteTodo(id: String)
    suspend fun syncTodos()
}
