package com.lifeos.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lifeos.data.local.converter.EnumConverter
import com.lifeos.data.local.dao.FinanceDao
import com.lifeos.data.local.dao.TodoDao
import com.lifeos.data.local.dao.TrackerDao
import com.lifeos.data.local.entity.FinanceEntity
import com.lifeos.data.local.entity.HabitLogEntity
import com.lifeos.data.local.entity.TodoEntity
import com.lifeos.data.local.entity.TrackerEntity

@Database(
    entities = [
        TodoEntity::class,
        FinanceEntity::class,
        TrackerEntity::class,
        HabitLogEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(EnumConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
    abstract fun financeDao(): FinanceDao
    abstract fun trackerDao(): TrackerDao
}
