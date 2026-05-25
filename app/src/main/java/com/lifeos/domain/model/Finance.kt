package com.lifeos.domain.model

data class Finance(
    val id: String,
    val type: FinanceType,
    val personName: String,
    val amount: Double,
    val currency: String,
    val date: Long,
    val isSettled: Boolean,
    val notes: String? = null,
    val createdAt: Long,
    val updatedAt: Long,
    val syncStatus: SyncStatus = SyncStatus.LOCAL_ONLY
)
