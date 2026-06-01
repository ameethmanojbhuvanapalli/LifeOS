package com.lifeos.domain.usecase.todo

import com.lifeos.domain.repository.TodoRepository
import javax.inject.Inject

class SyncTodosUseCase @Inject constructor(
    private val repository: TodoRepository
) {
    suspend operator fun invoke() = repository.syncTodos()
}
