package com.lifeos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lifeos.domain.model.FinanceType
import com.lifeos.domain.model.SyncStatus

@Entity(tableName = "finance_records")
data class FinanceEntity(
    @PrimaryKey val id: String,
    val type: FinanceType,
    val personName: String,
    val amount: Double,
    val currency: String,
    val date: Long,
    val isSettled: Boolean,
    val notes: String?,
    val createdAt: Long,
    val updatedAt: Long,
    val syncStatus: SyncStatus
)
