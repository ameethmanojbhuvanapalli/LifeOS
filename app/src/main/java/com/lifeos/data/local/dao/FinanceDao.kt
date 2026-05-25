package com.lifeos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lifeos.data.local.entity.FinanceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FinanceDao {

    @Query("SELECT * FROM finance_records ORDER BY date DESC")
    fun getAll(): Flow<List<FinanceEntity>>

    @Query("SELECT * FROM finance_records WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): FinanceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: FinanceEntity)
}
