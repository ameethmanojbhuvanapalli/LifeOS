package com.lifeos.data.local.converter

import androidx.room.TypeConverter
import com.lifeos.domain.model.FinanceType
import com.lifeos.domain.model.Priority
import com.lifeos.domain.model.RepeatFrequency
import com.lifeos.domain.model.SyncStatus
import com.lifeos.domain.model.TrackerType

class EnumConverter {

    @TypeConverter fun fromPriority(value: Priority): String = value.name
    @TypeConverter fun toPriority(value: String): Priority = Priority.valueOf(value)

    @TypeConverter fun fromFinanceType(value: FinanceType): String = value.name
    @TypeConverter fun toFinanceType(value: String): FinanceType = FinanceType.valueOf(value)

    @TypeConverter fun fromTrackerType(value: TrackerType): String = value.name
    @TypeConverter fun toTrackerType(value: String): TrackerType = TrackerType.valueOf(value)

    @TypeConverter fun fromRepeatFrequency(value: RepeatFrequency): String = value.name
    @TypeConverter fun toRepeatFrequency(value: String): RepeatFrequency = RepeatFrequency.valueOf(value)

    @TypeConverter fun fromSyncStatus(value: SyncStatus): String = value.name
    @TypeConverter fun toSyncStatus(value: String): SyncStatus = SyncStatus.valueOf(value)
}
