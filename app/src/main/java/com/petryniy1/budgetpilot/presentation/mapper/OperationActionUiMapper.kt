package com.petryniy1.budgetpilot.presentation.mapper

import com.petryniy1.budgetpilot.domain.results.OperationActionResult
import com.petryniy1.budgetpilot.presentation.uiState.OperationActionError
import com.petryniy1.budgetpilot.presentation.uiState.OperationActionUiState



fun OperationActionResult.toOperationActionUiState(): OperationActionUiState {
    return when (this) {
        OperationActionResult.Success ->
            OperationActionUiState.Success

        OperationActionResult.AccountNotFound ->
            OperationActionUiState.Error(OperationActionError.AccountNotFound)

        OperationActionResult.InsufficientFunds ->
            OperationActionUiState.Error(OperationActionError.InsufficientFunds)

        OperationActionResult.CurrencyMismatch ->
            OperationActionUiState.Error(OperationActionError.CurrencyMismatch)

        OperationActionResult.DuplicateOperation ->
            OperationActionUiState.Error(OperationActionError.DuplicateOperation)

        OperationActionResult.OperationNotFound ->
            OperationActionUiState.Error(OperationActionError.OperationNotFound)

        is OperationActionResult.ValidationError ->
            OperationActionUiState.Error(OperationActionError.InvalidData)

        is OperationActionResult.Error ->
            OperationActionUiState.Error(OperationActionError.Unexpected)
    }
}