package com.petryniy1.budgetpilot.domain.models.results

sealed interface AccountActionResult {
    data object Success : AccountActionResult
    data object AccountNotFound : AccountActionResult
    data class ValidationError(
        val reason: AccountValidationResult
    ) : AccountActionResult

    data class Error(
        val throwable: Throwable
    ) : AccountActionResult
}