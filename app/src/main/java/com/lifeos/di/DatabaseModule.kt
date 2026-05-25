package com.lifeos.di

import android.content.Context
import androidx.room.Room
import com.lifeos.core.constants.AppConstants
import com.lifeos.data.local.AppDatabase
import com.lifeos.data.local.dao.FinanceDao
import com.lifeos.data.local.dao.TodoDao
import com.lifeos.data.local.dao.TrackerDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppConstants.DATABASE_NAME
        ).build()
    }

    @Provides fun provideTodoDao(db: AppDatabase): TodoDao = db.todoDao()
    @Provides fun provideFinanceDao(db: AppDatabase): FinanceDao = db.financeDao()
    @Provides fun provideTrackerDao(db: AppDatabase): TrackerDao = db.trackerDao()
}
