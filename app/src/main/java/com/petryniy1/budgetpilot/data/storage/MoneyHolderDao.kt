package com.petryniy1.budgetpilot.data.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import com.petryniy1.budgetpilot.data.storage.models.AccountEntity

@Dao
interface MoneyHolderDao {
    @Query("SELECT*FROM moneyHolder")
    fun getMoneyHolders(): Flow<List<AccountEntity>>

    @Query("SELECT*FROM moneyHolder WHERE moneyId = :id")
    suspend fun getMoneyHolderById(id: Int): AccountEntity

    @Insert
    suspend fun addMoneyHolder(accountEntity: AccountEntity)

    @Update
    suspend fun updateMoneyHolder(accountEntity: AccountEntity)

    @Query("DELETE FROM moneyHolder WHERE moneyId = :id")
    suspend fun deleteMoneyHolder(id: Int)

    @Query("SELECT SUM(balance) FROM moneyHolder")
    fun getMoneyHoldersSumBalance(): Flow<Long>
}