package com.petryniy1.budgetpilot.presentation.uiState

sealed interface AccountActionUiState {
    data object Ready : AccountActionUiState
    data object Loading : AccountActionUiState
    data object Success : AccountActionUiState

    data class Error(
        val reason: AccountActionError
    ) : AccountActionUiState
}
