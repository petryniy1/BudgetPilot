package com.petryniy1.budgetpilot.domain.repository

import kotlinx.coroutines.flow.Flow
import com.petryniy1.budgetpilot.domain.models.MoneyHolder

interface MoneyHoldersRepository {

    fun getMoneyHolders(): Flow<List<MoneyHolder?>>

    suspend fun getMoneyHolderById(id: Int): MoneyHolder?

    suspend fun addMoneyHolder(moneyHolder: MoneyHolder)

    suspend fun updateMoneyHolder(moneyHolder: MoneyHolder)

    suspend fun deleteMoneyHolder(id: Int)

    fun getMoneyHoldersSumBalance(): Flow<Long?>
}