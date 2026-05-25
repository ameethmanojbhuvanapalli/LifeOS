package com.lifeos.domain.model

data class Tracker(
    val id: String,
    val name: String,
    val description: String? = null,
    val type: TrackerType,
    val targetValue: Int? = null,
    val unit: String? = null,
    val frequency: RepeatFrequency,
    val createdAt: Long,
    val syncStatus: SyncStatus = SyncStatus.LOCAL_ONLY
)
