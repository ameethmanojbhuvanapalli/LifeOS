package com.lifeos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lifeos.domain.model.RepeatFrequency
import com.lifeos.domain.model.SyncStatus
import com.lifeos.domain.model.TrackerType

@Entity(tableName = "trackers")
data class TrackerEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String?,
    val type: TrackerType,
    val targetValue: Int? = null,
    val unit: String? = null,
    val frequency: RepeatFrequency,
    val createdAt: Long,
    val syncStatus: SyncStatus
)
