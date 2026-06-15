package com.petryniy1.budgetpilot.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.petryniy1.budgetpilot.data.storage.AppDatabase
import com.petryniy1.budgetpilot.data.storage.dao.AccountDao
import com.petryniy1.budgetpilot.data.storage.dao.BudgetOperationDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase =
        Room.databaseBuilder(
            appContext,
            AppDatabase::class.java, "USERDATA"
        )
            .addMigrations(
                AppDatabase.MIGRATION_1_2,
                AppDatabase.MIGRATION_2_3
            )
            .build()

    @Singleton
    @Provides
    fun provideAccountDao(appDatabase: AppDatabase): AccountDao =
        appDatabase.getAccountDao()

    @Singleton
    @Provides
    fun provideBudgetOperationDao(appDatabase: AppDatabase): BudgetOperationDao =
        appDatabase.getBudgetOperationDao()
}
