package com.petryniy1.budgetpilot.domain.repository

import com.petryniy1.budgetpilot.domain.models.Account
import com.petryniy1.budgetpilot.domain.models.BudgetOperation
import com.petryniy1.budgetpilot.domain.models.results.OperationActionResult
import kotlinx.coroutines.flow.Flow

interface BudgetOperationRepository {
    fun observeOperations(): Flow<List<BudgetOperation>>

    fun observeOperation(id: Int): Flow<BudgetOperation?>

    fun observeOperationsByAccount(accountId: Int): Flow<List<BudgetOperation>>

    suspend fun findOperation(id: Int): BudgetOperation?

    suspend fun createOperation(operation: BudgetOperation): OperationActionResult

    suspend fun createOperationAndUpdateAccount(
        operation: BudgetOperation, updatedAccount: Account
    ): OperationActionResult

    suspend fun deleteOperationAndUpdateAccount(
        operation: BudgetOperation, updatedAccount: Account
    ): OperationActionResult

    suspend fun deleteOperation(id: Int): OperationActionResult
}
