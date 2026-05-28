package com.petryniy1.budgetpilot.domain.repository

import kotlinx.coroutines.flow.Flow
import com.petryniy1.budgetpilot.domain.models.Operation
import com.petryniy1.budgetpilot.domain.models.OperationWithMoneyHolder

interface OperationsRepository {

    fun getAllOperations(): Flow<List<OperationWithMoneyHolder?>>

    fun getOperationById(id: Int): Flow<OperationWithMoneyHolder?>

    suspend fun addOperation(operation: Operation)

    suspend fun updateOperation(operation: Operation)

    suspend fun deleteOperation(id: Int)

    fun getOperationsSumValue(): Flow<Long?>

}