package com.lifeos.data.mapper

import com.lifeos.data.local.entity.TodoEntity
import com.lifeos.domain.model.Todo

object TodoMapper {
    fun toDomain(entity: TodoEntity): Todo = Todo(
        id = entity.id,
        title = entity.title,
        description = entity.description,
        isCompleted = entity.isCompleted,
        priority = entity.priority,
        dueDate = entity.dueDate,
        createdAt = entity.createdAt,
        updatedAt = entity.updatedAt,
        syncStatus = entity.syncStatus
    )

    fun toEntity(domain: Todo): TodoEntity = TodoEntity(
        id = domain.id,
        title = domain.title,
        description = domain.description,
        isCompleted = domain.isCompleted,
        priority = domain.priority,
        dueDate = domain.dueDate,
        createdAt = domain.createdAt,
        updatedAt = domain.updatedAt,
        syncStatus = domain.syncStatus
    )
}
