package com.petryniy1.budgetpilot.presentation.uiState

sealed interface OperationActionUiState {
    data object Ready : OperationActionUiState
    data object Loading : OperationActionUiState
    data object Success : OperationActionUiState
    data class Error(val reason: OperationActionError) : OperationActionUiState
}

sealed interface OperationActionError {
    data object AccountNotFound : OperationActionError
    data object InsufficientFunds : OperationActionError
    data object CurrencyMismatch : OperationActionError
    data object DuplicateOperation : OperationActionError
    data object OperationNotFound : OperationActionError
    data object InvalidData : OperationActionError
    data object Unexpected : OperationActionError
}
