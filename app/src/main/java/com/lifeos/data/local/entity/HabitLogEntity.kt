package com.lifeos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habit_logs")
data class HabitLogEntity(
    @PrimaryKey val id: String,
    val trackerId: String,
    val value: Int,
    val timestamp: Long,
    val notes: String?
)
