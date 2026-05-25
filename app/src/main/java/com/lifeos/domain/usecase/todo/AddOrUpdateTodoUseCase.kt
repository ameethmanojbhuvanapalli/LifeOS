package com.lifeos.domain.usecase.todo

import com.lifeos.domain.model.Todo
import com.lifeos.domain.repository.TodoRepository
import javax.inject.Inject

class AddOrUpdateTodoUseCase @Inject constructor(
    private val repository: TodoRepository
) {
    suspend operator fun invoke(todo: Todo) = repository.upsertTodo(todo)
}
