package com.petryniy1.budgetpilot.domain.validation

import com.petryniy1.budgetpilot.domain.models.Account
import com.petryniy1.budgetpilot.domain.results.AccountValidationResult

class AccountValidator {
    fun validate(account: Account): AccountValidationResult {
        return when {
            account.name.isBlank() -> AccountValidationResult.EmptyName
            account.balance.amountMinor < 0 -> AccountValidationResult.NegativeBalance
            else -> AccountValidationResult.Valid
        }
    }
}