package com.petryniy1.budgetpilot.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.petryniy1.budgetpilot.data.storage.AppDatabase
import com.petryniy1.budgetpilot.data.storage.MoneyHolderDao
import com.petryniy1.budgetpilot.data.storage.OperationsDAO
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase =
        Room.databaseBuilder(appContext, AppDatabase::class.java, "USERDATA").build()

    @Singleton
    @Provides
    fun provideOperationsDao(appDatabase: AppDatabase): OperationsDAO =
        appDatabase.getOperationsDAO()

    @Singleton
    @Provides
    fun provideMoneyHolderDao(appDatabase: AppDatabase): MoneyHolderDao =
        appDatabase.getMoneyHolderDAO()
}