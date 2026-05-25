package com.lifeos.domain.usecase.todo

import com.lifeos.domain.repository.TodoRepository
import javax.inject.Inject

class GetTodosUseCase @Inject constructor(
    private val repository: TodoRepository
) {
    operator fun invoke() = repository.getAllTodos()
}
