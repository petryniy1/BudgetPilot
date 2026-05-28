package com.petryniy1.budgetpilot.data.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import com.petryniy1.budgetpilot.data.storage.models.MoneyHolderEntity

@Dao
interface MoneyHolderDao {
    @Query("SELECT*FROM moneyHolder")
    fun getMoneyHolders(): Flow<List<MoneyHolderEntity>>

    @Query("SELECT*FROM moneyHolder WHERE moneyId = :id")
    suspend fun getMoneyHolderById(id: Int): MoneyHolderEntity

    @Insert
    suspend fun addMoneyHolder(moneyHolderEntity: MoneyHolderEntity)

    @Update
    suspend fun updateMoneyHolder(moneyHolderEntity: MoneyHolderEntity)

    @Query("DELETE FROM moneyHolder WHERE moneyId = :id")
    suspend fun deleteMoneyHolder(id: Int)

    @Query("SELECT SUM(balance) FROM moneyHolder")
    fun getMoneyHoldersSumBalance(): Flow<Long>
}