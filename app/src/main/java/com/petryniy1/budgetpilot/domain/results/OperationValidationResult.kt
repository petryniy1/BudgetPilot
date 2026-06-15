package com.petryniy1.budgetpilot.domain.results

sealed interface OperationValidationResult {
    data object Valid : OperationValidationResult
    data object EmptyTitle : OperationValidationResult
    data object NonPositiveAmount : OperationValidationResult
}