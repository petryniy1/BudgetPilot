package com.petryniy1.budgetpilot.data.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import com.petryniy1.budgetpilot.data.storage.models.AccountEntity

@Dao
interface MoneyHolderDao {
    @Query("SELECT * FROM accounts")
    fun getAccounts(): Flow<List<AccountEntity>>

    @Query("SELECT * FROM accounts WHERE id = :id")
    suspend fun getAccountById(id: Int): AccountEntity

    @Insert
    suspend fun addAccount(accountEntity: AccountEntity)

    @Update
    suspend fun updateAccount(accountEntity: AccountEntity)

    @Query("DELETE FROM accounts WHERE id = :id")
    suspend fun deleteAccount(id: Int)

    @Query("SELECT COALESCE(SUM(balance_minor), 0) FROM accounts")
    fun getAccountTotalBalance(): Flow<Long>
}