package com.petryniy1.budgetpilot.domain.validation

import com.petryniy1.budgetpilot.domain.models.BudgetOperation
import com.petryniy1.budgetpilot.domain.results.OperationValidationResult

class OperationValidator {

    fun validate(operation: BudgetOperation): OperationValidationResult {
        return when {
            operation.title.isBlank() -> OperationValidationResult.EmptyTitle
            operation.amount.amountMinor <= 0 -> OperationValidationResult.NonPositiveAmount
            else -> OperationValidationResult.Valid
        }
    }
}