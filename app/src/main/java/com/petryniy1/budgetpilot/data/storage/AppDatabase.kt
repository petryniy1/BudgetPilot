package com.petryniy1.budgetpilot.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.petryniy1.budgetpilot.data.storage.models.MoneyHolderEntity
import com.petryniy1.budgetpilot.data.storage.models.OperationEntity

@Database(
    entities = [
        OperationEntity::class,
        MoneyHolderEntity::class
    ],
    exportSchema = false,
    version = AppDatabase.VERSION
)

abstract class AppDatabase : RoomDatabase() {
    companion object {
        const val VERSION = 1
    }

    abstract fun getOperationsDAO(): OperationsDAO

    abstract fun getMoneyHolderDAO(): MoneyHolderDao
}