package com.lifeos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lifeos.domain.model.Priority
import com.lifeos.domain.model.SyncStatus

@Entity(tableName = "todos")
data class TodoEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String?,
    val isCompleted: Boolean,
    val priority: Priority,
    val dueDate: Long?,
    val createdAt: Long,
    val updatedAt: Long,
    val syncStatus: SyncStatus
)
