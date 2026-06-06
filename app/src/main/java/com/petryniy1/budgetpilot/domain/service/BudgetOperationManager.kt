package com.petryniy1.budgetpilot.domain.service

import com.petryniy1.budgetpilot.domain.models.BudgetOperation
import com.petryniy1.budgetpilot.domain.models.results.OperationActionResult

interface BudgetOperationManager {
    suspend fun addOperation(operation: BudgetOperation): OperationActionResult

    suspend fun updateOperation(operation: BudgetOperation): OperationActionResult

    suspend fun deleteOperation(id: Int): OperationActionResult
}