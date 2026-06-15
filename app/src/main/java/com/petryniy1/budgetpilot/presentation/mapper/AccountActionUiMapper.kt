package com.petryniy1.budgetpilot.presentation.mapper

import com.petryniy1.budgetpilot.domain.results.AccountActionResult
import com.petryniy1.budgetpilot.domain.results.AccountValidationResult
import com.petryniy1.budgetpilot.presentation.uiState.AccountActionError
import com.petryniy1.budgetpilot.presentation.uiState.AccountActionUiState

fun AccountActionResult.toAccountActionUiState(
    successMessage: String
): AccountActionUiState {
    return when (this) {
        AccountActionResult.Success -> AccountActionUiState.Success(successMessage)

        AccountActionResult.AccountNotFound -> {
            AccountActionUiState.Error(AccountActionError.AccountNotFound)
        }

        AccountActionResult.DuplicateAccountName -> {
            AccountActionUiState.Error(AccountActionError.DuplicateAccountName)
        }

        is AccountActionResult.ValidationError -> {
            when (reason) {
                AccountValidationResult.EmptyName -> {
                    AccountActionUiState.Error(AccountActionError.EmptyName)
                }

                AccountValidationResult.NegativeBalance -> {
                    AccountActionUiState.Error(AccountActionError.NegativeBalance)
                }

                AccountValidationResult.Valid -> {
                    AccountActionUiState.Error(AccountActionError.Unexpected)
                }
            }
        }

        is AccountActionResult.Error -> {
            AccountActionUiState.Error(AccountActionError.Unexpected)
        }
    }
}