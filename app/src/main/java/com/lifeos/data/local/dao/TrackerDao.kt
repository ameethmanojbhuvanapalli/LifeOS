package com.lifeos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lifeos.data.local.entity.HabitLogEntity
import com.lifeos.data.local.entity.TrackerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackerDao {

    @Query("SELECT * FROM trackers ORDER BY createdAt DESC")
    fun getAllTrackers(): Flow<List<TrackerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTracker(entity: TrackerEntity)

    @Query("SELECT * FROM habit_logs WHERE trackerId = :trackerId ORDER BY timestamp DESC")
    fun getHabitLogs(trackerId: String): Flow<List<HabitLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabitLog(entity: HabitLogEntity)
}
