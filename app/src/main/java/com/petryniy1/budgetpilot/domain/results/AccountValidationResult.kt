package com.petryniy1.budgetpilot.domain.results

sealed interface AccountValidationResult {
    data object Valid : AccountValidationResult
    data object EmptyName : AccountValidationResult
    data object NegativeBalance : AccountValidationResult
}