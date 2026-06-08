package com.petryniy1.budgetpilot.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.petryniy1.budgetpilot.data.storage.dao.AccountDao
import com.petryniy1.budgetpilot.data.storage.dao.BudgetOperationDao
import com.petryniy1.budgetpilot.data.storage.models.AccountEntity
import com.petryniy1.budgetpilot.data.storage.models.OperationEntity
import com.petryniy1.budgetpilot.data.storage.models.BudgetOperationEntity

@Database(
    entities = [
        OperationEntity::class,
        AccountEntity::class,
        BudgetOperationEntity::class
    ],
    exportSchema = false,
    version = AppDatabase.VERSION
)

abstract class AppDatabase : RoomDatabase() {
    companion object {
        const val VERSION = 2

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                        CREATE TABLE IF NOT EXISTS budget_operations_v1 (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        account_id INTEGER NOT NULL,
                        title TEXT NOT NULL,
                        amount_minor INTEGER NOT NULL,
                        currency_code TEXT NOT NULL,
                        type TEXT NOT NULL,
                        date_epoch_day INTEGER NOT NULL,
                        comment TEXT NOT NULL,
                        FOREIGN KEY(account_id) REFERENCES accounts(id) ON DELETE RESTRICT
                        )
                    """.trimIndent()
                )

                db.execSQL(
                    """
                        CREATE INDEX IF NOT EXISTS index_budget_operations_v1_account_id
                        ON budget_operations_v1(account_id)
                    """.trimIndent()
                )
            }
        }
    }
    abstract fun getOperationsDAO(): OperationsDAO

    abstract fun getMoneyHolderDAO(): MoneyHolderDao

    abstract fun getAccountDao(): AccountDao

    abstract fun getBudgetOperationDao(): BudgetOperationDao
}