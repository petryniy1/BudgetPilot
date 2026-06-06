package com.petryniy1.budgetpilot.domain.models.results

sealed interface OperationActionResult {
    data object Success : OperationActionResult
    data object OperationNotFound : OperationActionResult
    data object AccountNotFound : OperationActionResult
    data object InsufficientFunds : OperationActionResult
    data object CurrencyMismatch : OperationActionResult
    data object DuplicateOperation : OperationActionResult

    data class Error(
        val throwable: Throwable
    ) : OperationActionResult
}
